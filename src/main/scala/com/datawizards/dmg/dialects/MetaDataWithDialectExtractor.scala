package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.MetaDataExtractor.extractClassMetaData
import com.datawizards.dmg.metadata._

import scala.reflect.runtime.universe.TypeTag

object MetaDataWithDialectExtractor {
  /**
    * Extract class metadata and change types and fields taking into account dialect as context.
    * @param dialect rename class and field names according to dialect. If dialect is null, then
    */
  def extractClassMetaDataForDialect[T: TypeTag](dialect: Option[Dialect]): ClassTypeMetaData =
    changeName(dialect, extractClassMetaData[T]())

  private def changeName(dialect: Option[Dialect], c: ClassTypeMetaData): ClassTypeMetaData =
    c.copy(
      typeName = getClassName(dialect, c),
      fields = c.fields.map(f => changeName(dialect, f, c))
    )

  private def changeName(dialect: Option[Dialect], classFieldMetaData: ClassFieldMetaData, classTypeMetaData: ClassTypeMetaData): ClassFieldMetaData =
    classFieldMetaData.copy(
      fieldName = getFieldName(dialect, classFieldMetaData, classTypeMetaData),
      fieldType = changeName(dialect, classFieldMetaData.fieldType)
    )

  private def changeName(dialect: Option[Dialect], typeMetaData: TypeMetaData): TypeMetaData = typeMetaData match {
    case p:PrimitiveTypeMetaData => p
    case c:CollectionTypeMetaData => c
    case m:MapTypeMetaData => m
    case c:ClassTypeMetaData => changeName(dialect, c)
  }

  private val Table = "com.datawizards.dmg.annotations.table"
  private val Column = "com.datawizards.dmg.annotations.column"
  private val Underscore = "com.datawizards.dmg.annotations.underscore"


  private def getClassName(dialect: Option[Dialect], classMetaData: ClassTypeMetaData): String = {
    val dialectSpecificTableAnnotation = getAnnotationForDialect(Table, dialect, classMetaData)

    dialectSpecificTableAnnotation.map(a => a.attributes.filter(_.name == "name").head.value )
      .getOrElse(convertToUnderscoreIfRequired(classMetaData.typeName, dialect, classMetaData))
  }

  private def getFieldName(dialect: Option[Dialect], classFieldMetaData: ClassFieldMetaData, classTypeMetaData: ClassTypeMetaData): String = {
    val columnAnnotations = getAnnotationForDialect(Column, dialect, classFieldMetaData)

    columnAnnotations.map(a => a.attributes.filter(_.name == "name").head.value)
      .getOrElse(convertToUnderscoreIfRequired(classFieldMetaData.fieldName, dialect, classTypeMetaData))
  }

  private def convertToUnderscoreIfRequired(name: String, dialect: Option[Dialect], classTypeMetaData: ClassTypeMetaData): String = {
    val underscoreAnnotation: Option[AnnotationMetaData] = getAnnotationForDialect(Underscore, dialect, classTypeMetaData)
    underscoreAnnotation.map(a => name.replaceAll("(.)(\\p{Upper})","$1_$2").toLowerCase).getOrElse(name)
  }

  def getAnnotationForDialect(annotationName: String, dialect: Option[Dialect], hasAnnotations: HasAnnotations): Option[AnnotationMetaData] = {
    if(dialect.isDefined){
      val dialectName = dialect.get.getClass.getSimpleName.stripSuffix("$")
      val annotationsFiltered: Iterable[AnnotationMetaData] = hasAnnotations.annotations
        .filter(a => a.name == annotationName)

      val annotation: Option[AnnotationMetaData] = annotationsFiltered
        .find(a => a.attributes.find(_.name == "dialect").map(p => p.value.endsWith(dialectName)).getOrElse(false) )
        .orElse(annotationsFiltered.find(a => !a.attributes.exists(_.name == "dialect") ).headOption)
      annotation
    } else {
      val annotation: Option[AnnotationMetaData] = hasAnnotations.annotations
        .filter(a => a.name == annotationName)
        .find(a => !a.attributes.exists(_.name == "dialect") )
      annotation
    }
  }
}
