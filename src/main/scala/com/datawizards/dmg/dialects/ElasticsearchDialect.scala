package com.datawizards.dmg.dialects

import com.datawizards.dmg.model._

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

  override def structType: String = "N/A"

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    s"""{
       |   "mappings": {
       |      "${classMetaData.className}": {
       |         "properties": {
       |            ${generateFieldsExpression(classMetaData)}
       |         }
       |      }
       |   }
       |}""".stripMargin
  }

  private def generateFieldsExpression(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f => s"""${generateFieldExpression(f.name, f.targetType)}""")
      .mkString(",\n            ")

  private def generateFieldExpression(fieldName: String, fieldType: FieldType, level:Int=1): String = fieldType match {
    case p:PrimitiveFieldType => s""""$fieldName": {"type": "${p.name}"}"""
    case a:ArrayFieldType => generateFieldExpression(fieldName, a.elementType)
    case s:StructFieldType =>
      s""""$fieldName": {
         |         ${"   "*level}   "properties": {
         |            ${"   "*level}   ${s.fields.map{case (k,v) => s"""${generateFieldExpression(k, v, level+1)}"""}.mkString(",\n               "+("   "*level))}
         |            ${"   "*level}}
         |         ${"   "*level}}""".stripMargin
  }

  override def toString: String = "ElasticsearchDialect"

}
