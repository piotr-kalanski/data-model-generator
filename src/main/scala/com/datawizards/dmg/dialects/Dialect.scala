package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._
import org.apache.log4j.Logger

trait Dialect {
  protected val log:Logger = Logger.getLogger(getClass.getName)

  private val Length = "com.datawizards.dmg.annotations.length"
  private val Comment = "com.datawizards.dmg.annotations.comment"
  private val NotNull = "com.datawizards.dmg.annotations.notNull"

  def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String

  def mapPrimitiveDataType(primitiveType: PrimitiveTypeMetaData): String = primitiveType match {
    case IntegerType => intType
    case StringType => stringType
    case LongType => longType
    case DoubleType => doubleType
    case FloatType => floatType
    case ShortType => shortType
    case BooleanType => booleanType
    case ByteType => byteType
    case DateType => dateType
    case TimestampType => timestampType
    case BinaryType => binaryType
    case BigDecimalType => bigDecimalType
    case BigIntegerType => bigIntegerType
    case _ => throw new Exception("Not supported type: " + primitiveType)
  }

  def intType: String
  def stringType: String
  def longType: String
  def doubleType: String
  def floatType: String
  def shortType: String
  def booleanType: String
  def byteType: String
  def dateType: String
  def timestampType: String
  def binaryType: String
  def bigDecimalType: String
  def bigIntegerType: String

  def fieldLength(f: ClassFieldMetaData): Option[String] = MetaDataWithDialectExtractor.getAnnotationForDialect(Length, Some(this), f).map(_.getValue)

  def comment(a: HasAnnotations): Option[String] = MetaDataWithDialectExtractor.getAnnotationForDialect(Comment, Some(this), a).map(_.getValue)

  def notNull(f: ClassFieldMetaData): Boolean = MetaDataWithDialectExtractor.getAnnotationForDialect(NotNull, Some(this), f).nonEmpty

  def generateClassFieldExpression(f: ClassFieldMetaData): String =
    generateClassFieldExpression(f, 0)

  def generateClassFieldExpression(f: ClassFieldMetaData, level: Int): String = {
    val typeExpression = generateTypeExpression(f.fieldType, level)
    generateClassFieldExpression(f, typeExpression, level)
  }

  def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String): String =
    generateClassFieldExpression(f, typeExpression, 0)

  def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String

  def generateTypeExpression(typeMetaData: TypeMetaData): String =
    generateTypeExpression(typeMetaData, 0)

  def generateTypeExpression(typeMetaData: TypeMetaData, level: Int): String = typeMetaData match {
    case p:PrimitiveTypeMetaData => generatePrimitiveTypeExpression(p)
    case c:CollectionTypeMetaData => generateArrayTypeExpression(generateTypeExpression(c.elementType, level))
    case m:MapTypeMetaData => generateMapTypeExpression(generateTypeExpression(m.keyType, level), generateTypeExpression(m.valueType, level))
    case c:ClassTypeMetaData => generateClassTypeExpression(c, c.fields.map(f => (f.fieldName, generateClassFieldExpression(f, level+1))))
  }

  def generatePrimitiveTypeExpression(p:PrimitiveTypeMetaData): String =
    mapPrimitiveDataType(p)

  def generateArrayTypeExpression(elementTypeExpression: String): String

  def generateMapTypeExpression(keyExpression: String, valueExpression: String): String

  def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String

  def generateColumn(f: ClassFieldMetaData): Boolean = true
}


