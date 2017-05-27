package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._
import com.datawizards.dmg.model._
import org.apache.log4j.Logger

trait Dialect {
  protected val log:Logger = Logger.getLogger(getClass.getName)

  def generateDataModel(classMetaData: ClassMetaData): String

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

  def generateTypeExpression(fieldMetaData: FieldMetaData): String = generateTypeExpression(fieldMetaData.fieldType)

  def generateTypeExpression(typeMetaData: TypeMetaData): String = typeMetaData match {
    case p:PrimitiveTypeMetaData => generatePrimitiveTypeExpression(mapPrimitiveDataType(p))
    case c:CollectionTypeMetaData => generateArrayTypeExpression(generateTypeExpression(c.elementType))
    //case m:MapTypeMetaData => generateMapTypeExpression(generateTypeExpression(m.keyType), generateTypeExpression(m.valueType))
    case c:ClassTypeMetaData => generateClassTypeExpression(c, c.fields.map(f => (f.fieldName, generateTypeExpression(f.fieldType))))
    case _ => throw new Exception("Not supported")
  }

  def generatePrimitiveTypeExpression(typeExpression: String): String = typeExpression

  def generateArrayTypeExpression(elementTypeExpression: String): String

  //protected def generateMapTypeExpression(keyTypeExpression: String, valueTypeExpression: String): String

  def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String
}


