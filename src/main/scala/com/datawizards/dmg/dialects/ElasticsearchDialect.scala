package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.ClassMetaData

object ElasticsearchDialect extends Dialect {

  override def intType: String = "integer"

  override def stringType: String = "string"

  override def longType: String = "long"

  override def doubleType: String = "double"

  override def floatType: String = "float"

  override def shortType: String = "short"

  override def booleanType: String = "boolean"

  override def byteType: String = "byte"

  override def dateType: String = "date"

  override def timestampType: String = "date"

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val fieldsExpression = generateFieldsExpression(classMetaData  )

    s"""{
       |   "mappings": {
       |      "${classMetaData.className}": {
       |         $fieldsExpression
       |      }
       |   }
       |}""".stripMargin
  }

  private def generateFieldsExpression(classMetaData: ClassMetaData): String =
    classMetaData.fields.map(f => s""""${f.name}": {"type": "${f.targetType}"}""").mkString(",\n         ")
}
