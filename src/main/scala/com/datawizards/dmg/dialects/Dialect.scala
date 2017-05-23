package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldType, PrimitiveFieldType}
import org.apache.spark.sql.types._

trait Dialect {

  def generateDataModel(classMetaData: ClassMetaData): String

  def mapDataType(dataType: DataType): FieldType = dataType match {
    case IntegerType => PrimitiveFieldType(intType)
    case StringType => PrimitiveFieldType(stringType)
    case LongType => PrimitiveFieldType(longType)
    case DoubleType => PrimitiveFieldType(doubleType)
    case FloatType => PrimitiveFieldType(floatType)
    case ShortType => PrimitiveFieldType(shortType)
    case BooleanType => PrimitiveFieldType(booleanType)
    case ByteType => PrimitiveFieldType(byteType)
    case DateType => PrimitiveFieldType(dateType)
    case TimestampType => PrimitiveFieldType(timestampType)
    case a:ArrayType => ArrayFieldType(arrayType, mapDataType(a.elementType))
    case _ => throw new Exception("Not supported type: " + dataType)
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
  def arrayType: String
}


