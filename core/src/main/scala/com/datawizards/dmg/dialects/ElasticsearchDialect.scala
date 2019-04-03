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

  override def binaryType: String = byteType

  override def bigDecimalType: String = "double"

  override def bigIntegerType: String = "double"

  override def toString: String = "ElasticsearchDialect"

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    "{\n" +
       templateExpression(classTypeMetaData) +
       settingsExpression(classTypeMetaData) +
       mappingsExpression(classTypeMetaData, fieldsExpressions) +
    "\n}"

  private def templateExpression(classTypeMetaData: ClassTypeMetaData): String = {
    val template = classTypeMetaData.getAnnotationValue(EsTemplate)
    if(template.isEmpty) ""
    else
      s"""
         |   "template" : "${template.get}",""".stripMargin
  }

  private def settingsExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val settings = classTypeMetaData.annotations.filter(_.name == EsSetting)
    if(settings.isEmpty)
      ""
    else
      "\n   \"settings\" : {\n      " +
        settings
          .map(a => {
            val key = a.attributes.find(_.name == "key").get.value
            val value = a.attributes.find(_.name == "value").get.value
            s""""$key" : ${addApostrophesIfRequired(value)}"""
          })
          .mkString(",\n      ") +
        "\n   },"
  }

  private def mappingsExpression(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    s"""
       |   "mappings" : {
       |      "${classTypeMetaData.typeName}" : {
       |         "properties" : {
       |            ${fieldsExpressions.mkString(",\n            ")}
       |         }
       |      }
       |   }""".stripMargin

  private val EsTemplate: String = "com.datawizards.dmg.annotations.es.esTemplate"
  private val EsSetting: String = "com.datawizards.dmg.annotations.es.esSetting"

  private def addApostrophesIfRequired(value: String): String =
    if(value.exists(_.isLetter)) "\"" + value + "\""
    else value

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    s""""${f.fieldName}" : """ + (f.fieldType match {
      case _:ClassTypeMetaData => typeExpression
      case c:CollectionTypeMetaData => c.elementType match {
        case _:ClassTypeMetaData => typeExpression
        case _ => s"""{"type" : $typeExpression}"""
      }
      case _ => s"""{"type" : $typeExpression${fieldParametersExpressions(f)}}"""
    })

  override def generatePrimitiveTypeExpression(p: PrimitiveTypeMetaData): String =
    s""""${mapPrimitiveDataType(p)}""""

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    elementTypeExpression

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s"""{"properties" : {${fieldNamesWithExpressions.map{case (k,v) => v}.mkString(", ")}}}"""

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String = {
    log.warn("Elasticsearch doesn't support Map type. Column converted to string")
    "\"string\""
  }

  private def fieldParametersExpressions(f: ClassFieldMetaData): String =
    indexParameterExpression(f) +
      formatParameterExpression(f)

  private def indexParameterExpression(f: ClassFieldMetaData): String =
    generateFieldParameterExpression(f, "com.datawizards.dmg.annotations.es.esIndex", "index")

  private def formatParameterExpression(f: ClassFieldMetaData): String =
    generateFieldParameterExpression(f, "com.datawizards.dmg.annotations.es.esFormat", "format")

  private def generateFieldParameterExpression(f: ClassFieldMetaData, annotationName: String, parameterName: String): String = {
    val annotation = f.getAnnotationValue(annotationName)
    if(annotation.isDefined) s""", "$parameterName" : "${annotation.get}""""
    else ""
  }
}
