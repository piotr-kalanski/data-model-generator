package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldType, PrimitiveFieldType}

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

  override def arrayType: String = "array"

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val fieldsExpression = generateFieldsExpression(classMetaData)
    val tableDoc =
      if(classMetaData.comment.isEmpty) ""
      else s"""
         |   "doc": "${classMetaData.comment.get}",""".stripMargin

    s"""{
       |   "namespace": "${classMetaData.packageName}",
       |   "type": "record",
       |   "name": "${classMetaData.className}",$tableDoc
       |   "fields": [
       |      $fieldsExpression
       |   ]
       |}""".stripMargin
  }

  private def generateFieldsExpression(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f =>
        s"""{"name": "${f.name}", """ +
        s""""type": "${f.targetType.name}"""" +
          (f.targetType match {
              case a:ArrayFieldType => s""", "items": ${getArrayItemsType(a.elementType)}"""
              case _ => ""
          }) +
        s"""${if(f.comment.isEmpty) "" else s""", "doc": "${f.comment.get}""""}}""".stripMargin
      )
      .mkString(",\n      ")

  private def getArrayItemsType(fieldType: FieldType): String = fieldType match {
    case p:PrimitiveFieldType => s""""${p.name}""""
    case a:ArrayFieldType => s"""{"type": "array", "items": ${getArrayItemsType(a.elementType)}}"""
  }

  override def toString: String = "AvroSchemaDialect"

}
