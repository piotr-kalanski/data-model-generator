package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.ClassTypeMetaData
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

  override def toString: String = "ElasticsearchDialect"

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
      .map(f => s""""${f.name}": ${generateTypeExpression(f)}""")
      .mkString(",\n            ")

  override def generatePrimitiveTypeExpression(typeExpression: String): String =
    s"""{"type": "$typeExpression"}"""

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    elementTypeExpression

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s"""{"properties": {${fieldNamesWithExpressions.map{case (k,v) => s""""$k": $v"""}.mkString(", ")}}}"""
}
