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
    s"""{${settingsExpression(classTypeMetaData)}
       |   "mappings" : {
       |      "${classTypeMetaData.typeName}" : {
       |         "properties" : {
       |            ${fieldsExpressions.mkString(",\n            ")}
       |         }
       |      }
       |   }
       |}""".stripMargin

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

  private val EsSetting: String = "com.datawizards.dmg.annotations.es.esSetting"

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

  private def addApostrophesIfRequired(value: String): String =
    if(value.exists(_.isLetter)) "\"" + value + "\""
    else value

}
