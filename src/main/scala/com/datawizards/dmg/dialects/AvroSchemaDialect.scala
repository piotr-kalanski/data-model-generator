package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.{ClassTypeMetaData, CollectionTypeMetaData, PrimitiveTypeMetaData, TypeMetaData}
import com.datawizards.dmg.model._

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

  override def generatePrimitiveTypeExpression(typeExpression: String): String =
    s""""$typeExpression""""

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s""""array", "items": $elementTypeExpression"""

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s""""record", "fields": [${fieldNamesWithExpressions.map{case (k,v) => s"""{"name": "$k", "type": $v}"""}.mkString(", ")}]"""

  override def toString: String = "AvroSchemaDialect"

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
        s""""type": "${getAvroTypeName(f.fieldType)}"""" +
          (f.fieldType match {
              case a:CollectionTypeMetaData => s""", "items": ${getArrayItemsType(a.elementType)}"""
              case s:ClassTypeMetaData => s""", "fields": [${s.fields.map(f => s"""{"name": "${f.fieldName}", "type": ${generateTypeExpression(f.fieldType)}}""").mkString(", ")}]"""
              case _ => ""
          }) +
        s"""${if(f.comment.isEmpty) "" else s""", "doc": "${f.comment.get}""""}}""".stripMargin
      )
      .mkString(",\n      ")

  private def getArrayItemsType(typeMetaData: TypeMetaData): String = typeMetaData match {
    case p:PrimitiveTypeMetaData => s"""${generateTypeExpression(p)}"""
    case c:CollectionTypeMetaData => s"""{"type": "array", "items": ${getArrayItemsType(c.elementType)}}"""
    case c:ClassTypeMetaData => s"""{"type": "record", "fields": [${c.fields.map(f => s"""{"name": "${f.fieldName}", "type": ${generateTypeExpression(f.fieldType)}}""").mkString(", ")}]}"""
    case _ => throw new Exception("Not supported type: " + typeMetaData)
  }

  private def getAvroTypeName(typeMetaData: TypeMetaData): String  = typeMetaData match {
    case p:PrimitiveTypeMetaData => mapPrimitiveDataType(p)
    case c:CollectionTypeMetaData => "array"
    case c:ClassTypeMetaData => "record"
    case _ => throw new Exception("Not supported type: " + typeMetaData)
  }
}
