package com.datawizards.dmg.dialects

import com.datawizards.dmg.ClassMetaData
import org.apache.spark.sql.types._

trait Dialect {

  def generateDataModel(classMetaData: ClassMetaData): String

  def mapDataType(dataType: DataType): String = dataType match {
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
}


