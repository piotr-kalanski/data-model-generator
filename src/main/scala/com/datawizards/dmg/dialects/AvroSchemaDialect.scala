package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

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

  override def generatePrimitiveTypeExpression(p: PrimitiveTypeMetaData): String =
    s""""${mapPrimitiveDataType(p)}""""

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s""""array", "items": $elementTypeExpression"""

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s""""record", "fields": [${fieldNamesWithExpressions.map{case (k,v) => s"""{"name": "$k", "type": $v}"""}.mkString(", ")}]"""

  override def toString: String = "AvroSchemaDialect"

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String = {
    val fieldsExpression = fieldsExpressions.mkString(",\n      ")
    val tableDoc =
      if (comment(classTypeMetaData).isEmpty) ""
      else
        s"""
           |   "doc": "${comment(classTypeMetaData).get}",""".stripMargin

    s"""{
       |   "namespace": "${classTypeMetaData.packageName}",
       |   "type": "record",
       |   "name": "${classTypeMetaData.typeName}",$tableDoc
       |   "fields": [
       |      $fieldsExpression
       |   ]
       |}""".stripMargin
  }

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    s"""{"name": "${f.fieldName}", """ +
      s""""type": ${getAvroTypeName(f)}""" +
      (f.fieldType match {
        case a:CollectionTypeMetaData => s""", "items": ${getArrayItemsType(a.elementType)}"""
        case s:ClassTypeMetaData => s""", "fields": [${s.fields.map(f => s"""{"name": "${f.fieldName}", "type": ${generateTypeExpression(f.fieldType)}}""").mkString(", ")}]"""
        case _ => ""
      }) +
      s"""${if(comment(f).isEmpty) "" else s""", "doc": "${comment(f).get}""""}}"""

  private def getArrayItemsType(typeMetaData: TypeMetaData): String = typeMetaData match {
    case p:PrimitiveTypeMetaData => s"""${generateTypeExpression(p)}"""
    case c:CollectionTypeMetaData => s"""{"type": "array", "items": ${getArrayItemsType(c.elementType)}}"""
    case c:ClassTypeMetaData => s"""{"type": "record", "fields": [${c.fields.map(f => s"""{"name": "${f.fieldName}", "type": ${generateTypeExpression(f.fieldType)}}""").mkString(", ")}]}"""
    case _ => throw new Exception("Not supported type: " + typeMetaData)
  }

  private def getAvroTypeName(f: ClassFieldMetaData): String  = f.fieldType match {
    case p:PrimitiveTypeMetaData =>
      if(notNull(f)) s""""${mapPrimitiveDataType(p)}""""
      else s"""["null", "${mapPrimitiveDataType(p)}"]"""
    case c:CollectionTypeMetaData => "\"array\""
    case c:ClassTypeMetaData => "\"record\""
    case _ => throw new Exception("Not supported type: " + f.fieldType)
  }

}
