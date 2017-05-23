package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldType, PrimitiveFieldType}

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

  override def arrayType: String = "N/A"

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
    classMetaData
      .fields
      .map(f => s""""${f.name}": {"type": "${getFieldType(f.targetType)}"}""")
      .mkString(",\n         ")

  private def getFieldType(fieldType: FieldType): String = fieldType match {
    case p:PrimitiveFieldType => p.name
    case a:ArrayFieldType => getFieldType(a.elementType)
  }

  override def toString: String = "ElasticsearchDialect"
}
