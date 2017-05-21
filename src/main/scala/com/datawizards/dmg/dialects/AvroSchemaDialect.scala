package com.datawizards.dmg.dialects

import com.datawizards.dmg.ClassMetaData

object AvroSchemaDialect extends Dialect {

  override def intType: String = "int"

  override def stringType: String = "string"

  override def longType: String = "long"

  override def doubleType: String = "double"

  override def floatType: String = "float"

  override def shortType: String = "bytes"

  override def booleanType: String = "boolean"

  override def byteType: String = "bytes"

  override def dateType: String = "int"

  override def timestampType: String = "long"

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val fieldsExpression = generateFieldsExpression(classMetaData  )

    s"""{
       |   "namespace": "${classMetaData.packageName}",
       |   "type": "record",
       |   "name": "${classMetaData.className}",
       |   "fields": [
       |      $fieldsExpression
       |   ]
       |}""".stripMargin
  }

  private def generateFieldsExpression(classMetaData: ClassMetaData): String =
    classMetaData.fields.map(f => s"""{"name": "${f.name}", "type": "${f.targetType}"}""").mkString(",\n      ")
}
