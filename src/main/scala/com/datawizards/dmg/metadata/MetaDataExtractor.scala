package com.datawizards.dmg.metadata

import com.datawizards.dmg.dialects.Dialect

import scala.reflect.NameTransformer
import scala.reflect.runtime.universe._

/**
  * Class responsible for extracting metadata about case class: fields, annotations with attributes.
  * It is used to simplify getting such information, because current Scala API is very hard to use.
  */
object MetaDataExtractor {

  private def mirror =
    runtimeMirror(Thread.currentThread().getContextClassLoader)

  /**
    * Extract class metadata
    */
  def extractClassMetaData[T: TypeTag](): ClassTypeMetaData = {
    val tpe = localTypeOf[T]
    tpe match {
      case t if definedByConstructorParams(t) => extractClassMetaData(t)
      case other =>
        throw new UnsupportedOperationException(s"MetaData for type $other is not supported")
    }
  }

  /**
    * Extract class metadata and change types and fields taking into account dialect as context
    */
  def extractClassMetaDataForDialect[T: TypeTag](dialect: Dialect): ClassTypeMetaData =
    changeName(dialect, extractClassMetaData[T]())

  def extractTypeMetaData[T : TypeTag](): TypeMetaData =
    extractTypeMetaData(localTypeOf[T])

  private def extractTypeMetaData(tpe: `Type`): TypeMetaData = tpe match {
    case t if t <:< localTypeOf[Option[_]] =>
      val TypeRef(_, _, Seq(optType)) = t
      extractTypeMetaData(optType)
    case t if t <:< localTypeOf[Array[Byte]] => BinaryType
    case t if t <:< localTypeOf[Array[_]] =>
      val TypeRef(_, _, Seq(elementType)) = t
      CollectionTypeMetaData(extractTypeMetaData(elementType))
    case t if t <:< localTypeOf[Seq[_]] =>
      val TypeRef(_, _, Seq(elementType)) = t
      CollectionTypeMetaData(extractTypeMetaData(elementType))
    case t if t <:< localTypeOf[Map[_, _]] =>
      val TypeRef(_, _, Seq(keyType, valueType)) = t
      MapTypeMetaData(extractTypeMetaData(keyType), extractTypeMetaData(valueType))
    case t if t <:< localTypeOf[Iterable[_]] =>
      val TypeRef(_, _, Seq(elementType)) = t
      CollectionTypeMetaData(extractTypeMetaData(elementType))
    case t if t <:< localTypeOf[String] => StringType
    case t if t <:< localTypeOf[java.sql.Timestamp] => TimestampType
    case t if t <:< localTypeOf[java.util.Date] => DateType
    case t if t <:< localTypeOf[java.sql.Date] => DateType
    case t if t <:< localTypeOf[BigDecimal] => BigDecimalType
    case t if t <:< localTypeOf[java.math.BigDecimal] => BigDecimalType
    case t if t <:< localTypeOf[java.math.BigInteger] => BigIntegerType
    case t if t <:< localTypeOf[scala.math.BigInt] => BigIntegerType
    case t if t <:< localTypeOf[java.lang.Integer] => IntegerType
    case t if t <:< localTypeOf[java.lang.Long] => LongType
    case t if t <:< localTypeOf[java.lang.Double] => DoubleType
    case t if t <:< localTypeOf[java.lang.Float] => FloatType
    case t if t <:< localTypeOf[java.lang.Short] => ShortType
    case t if t <:< localTypeOf[java.lang.Byte] => ByteType
    case t if t <:< localTypeOf[java.lang.Boolean] => BooleanType
    case t if t <:< definitions.IntTpe => IntegerType
    case t if t <:< definitions.LongTpe => LongType
    case t if t <:< definitions.DoubleTpe => DoubleType
    case t if t <:< definitions.FloatTpe => FloatType
    case t if t <:< definitions.ShortTpe => ShortType
    case t if t <:< definitions.ByteTpe => ByteType
    case t if t <:< definitions.BooleanTpe => BooleanType
    case t if definedByConstructorParams(t) => extractClassMetaData(t)
    case other =>
      throw new UnsupportedOperationException(s"MetaData for type $other is not supported")
  }

  private def extractClassMetaData(tpe: `Type`): ClassTypeMetaData = {
    val cls = mirror.runtimeClass(tpe)
    ClassTypeMetaData(
      packageName = cls.getPackage.getName,
      originalTypeName = cls.getSimpleName,
      typeName = cls.getSimpleName,
      annotations = extractAnnotations(tpe.typeSymbol),
      fields = extractClassFields(tpe)
    )
  }

  private def localTypeOf[T: TypeTag]: Type = MetaDataExtractor.synchronized {
    val tag = implicitly[TypeTag[T]]
    tag.in(mirror).tpe.dealias
  }

  /**
    * Whether the fields of the given type is defined entirely by its constructor parameters.
    */
  private def definedByConstructorParams(tpe: Type): Boolean =
    tpe <:< localTypeOf[Product]

  private def extractClassFields(tpe: Type): Iterable[ClassFieldMetaData] =
    tpe
      .typeSymbol
      .asClass
      .primaryConstructor
      .typeSignature
      .paramLists
      .head
      .map(f => ClassFieldMetaData(
        originalFieldName = NameTransformer.decode(f.name.toString),
        fieldName = NameTransformer.decode(f.name.toString),
        fieldType = extractTypeMetaData(f.typeSignature),
        annotations = extractAnnotations(f)
      ))

  private def extractAnnotations(symbol: Symbol): Seq[AnnotationMetaData] =
    symbol
      .annotations
      .map(a =>
        AnnotationMetaData(
          name = a.tree.tpe.toString,
          attributes = {
            val params = a.tree.tpe.decls.find(_.name.toString == "<init>").get.asMethod.paramLists.flatten.map(f => f.name.toString)
            val values = a.tree.children.tail
            (params zip values)
              .map(pv => AnnotationAttributeMetaData(
                name = pv._1,
                value = pv._2.toString.stripPrefix("\"").stripSuffix("\"")
              ))
          }
        )
      )

  private def changeName(dialect: Dialect, c: ClassTypeMetaData): ClassTypeMetaData =
    c.copy(
      typeName = getClassName(dialect, c),
      fields = c.fields.map(f => changeName(dialect, f, c))
    )

  private def changeName(dialect: Dialect, classFieldMetaData: ClassFieldMetaData, classTypeMetaData: ClassTypeMetaData): ClassFieldMetaData =
    classFieldMetaData.copy(
      fieldName = getFieldName(dialect, classFieldMetaData, classTypeMetaData),
      fieldType = changeName(dialect, classFieldMetaData.fieldType)
    )

  private def changeName(dialect: Dialect, typeMetaData: TypeMetaData): TypeMetaData = typeMetaData match {
    case p:PrimitiveTypeMetaData => p
    case c:CollectionTypeMetaData => c
    case m:MapTypeMetaData => m
    case c:ClassTypeMetaData => changeName(dialect, c)
  }

  private val Table = "com.datawizards.dmg.annotations.table"
  private val Column = "com.datawizards.dmg.annotations.column"
  private val Underscore = "com.datawizards.dmg.annotations.underscore"

  private def getClassName(dialect: Dialect, classMetaData: ClassTypeMetaData): String = {
    val tableAnnotations = classMetaData.annotations.filter(_.name == Table)
    if(tableAnnotations.nonEmpty) {
      val dialectSpecificTableAnnotation = tableAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificTableAnnotation.isDefined)
        dialectSpecificTableAnnotation.get.attributes.filter(_.name == "name").head.value
      else {
        val defaultTableAnnotation = tableAnnotations.find(!_.attributes.exists(aa => aa.name == "dialect"))
        if(defaultTableAnnotation.isDefined)
          defaultTableAnnotation.get.attributes.filter(_.name == "name").head.value
        else
          convertToUnderscoreIfRequired(classMetaData.typeName, dialect, classMetaData)
      }
    }
    else
      convertToUnderscoreIfRequired(classMetaData.typeName, dialect, classMetaData)
  }

  private def getFieldName(dialect: Dialect, classFieldMetaData: ClassFieldMetaData, classTypeMetaData: ClassTypeMetaData): String = {
    val columnAnnotations = classFieldMetaData.annotations.filter(_.name == Column)
    if(columnAnnotations.nonEmpty) {
      val dialectSpecificColumnAnnotation = columnAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificColumnAnnotation.isDefined)
        dialectSpecificColumnAnnotation.get.attributes.filter(_.name == "name").head.value
      else {
        val defaultColumnAnnotation = columnAnnotations.find(!_.attributes.exists(aa => aa.name == "dialect"))
        if(defaultColumnAnnotation.isDefined)
          defaultColumnAnnotation.get.attributes.filter(_.name == "name").head.value
        else
          convertToUnderscoreIfRequired(classFieldMetaData.fieldName, dialect, classTypeMetaData)
      }
    }
    else
      convertToUnderscoreIfRequired(classFieldMetaData.fieldName, dialect, classTypeMetaData)
  }

  private def convertToUnderscoreIfRequired(name: String, dialect: Dialect, classTypeMetaData: ClassTypeMetaData): String = {
    val underscoreAnnotations = classTypeMetaData.annotations.filter(_.name == Underscore)
    if(underscoreAnnotations.nonEmpty) {
      val dialectSpecificUnderscoreAnnotation = underscoreAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificUnderscoreAnnotation.isDefined)
        name.replaceAll("(.)(\\p{Upper})","$1_$2").toLowerCase
      else
        name
    }
    else
      name
  }

}
