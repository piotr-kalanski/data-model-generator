package com.datawizards.dmg.dialects

import com.datawizards.dmg.model._
import org.apache.log4j.Logger
import org.apache.spark.sql.types._

trait Dialect {
  protected val log:Logger = Logger.getLogger(getClass.getName)

  def generateDataModel(classMetaData: ClassMetaData): String

  def mapDataType(typeName: String, dataType: DataType): FieldType = dataType match {
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
    case a:ArrayType => ArrayFieldType(arrayType, mapDataType(null /* not supported*/, a.elementType))
    case s:StructType => StructFieldType(structType, typeName, s.fields.map(x => x.name -> mapDataType(null /* not supported*/, x.dataType)).toMap)
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
  def structType: String
}


