package com.datawizards.dmg.dialects

import org.apache.spark.sql.types._

/*
  //TODO - add other dialects:
  object Hive extends DatabaseDialect
  object MySQL extends DatabaseDialect
  object Redshift extends DatabaseDialect
  object AvroSchema extends Dialect
  object ElasticsearchMapping extends Dialect
}*/

trait Dialect {

  case class FieldWithType(field: String, targetType: String)

  def generateDataModel(table: String, schema: StructType): String = {
    val fields = mapSchema(schema)
    generateDataModel(table, fields)
  }

  protected def generateDataModel(table: String, fields: Array[FieldWithType]): String

  private def mapSchema(schema: StructType): Array[FieldWithType] =
    schema
      .fields
      .map(f => FieldWithType(f.name, mapDataType(f.dataType)))

  private def mapDataType(dataType: DataType): String = dataType match {
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

  protected def intType: String
  protected def stringType: String
  protected def longType: String
  protected def doubleType: String
  protected def floatType: String
  protected def shortType: String
  protected def booleanType: String
  protected def byteType: String
  protected def dateType: String
  protected def timestampType: String
}


