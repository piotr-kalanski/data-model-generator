package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

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

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    s"""{
       |   "mappings": {
       |      "${classTypeMetaData.typeName}": {
       |         "properties": {
       |            ${fieldsExpressions.mkString(",\n            ")}
       |         }
       |      }
       |   }
       |}""".stripMargin

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    s""""${f.fieldName}": """ + (f.fieldType match {
    case _:ClassTypeMetaData => typeExpression
    case c:CollectionTypeMetaData => c.elementType match {
      case _:ClassTypeMetaData => typeExpression
      case _ => s"""{"type": $typeExpression}"""
    }
    case _ => s"""{"type": $typeExpression${indexAttribute(f)}}"""
  })

  override def generatePrimitiveTypeExpression(p: PrimitiveTypeMetaData): String =
    s""""${mapPrimitiveDataType(p)}""""

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    elementTypeExpression

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s"""{"properties": {${fieldNamesWithExpressions.map{case (k,v) => v}.mkString(", ")}}}"""

  private def indexAttribute(f: ClassFieldMetaData): String = {
    val indexAnnotation = f.getAnnotationValue("com.datawizards.dmg.annotations.es.esIndex")
    if(indexAnnotation.isDefined) s""", "index": "${indexAnnotation.get}""""
    else ""
  }
}
