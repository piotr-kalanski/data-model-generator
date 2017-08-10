package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

object JavaDialect extends Dialect {

  override def intType: String = "Integer"

  override def stringType: String = "String"

  override def longType: String = "Long"

  override def doubleType: String = "Double"

  override def floatType: String = "Float"

  override def shortType: String = "Short"

  override def booleanType: String = "Boolean"

  override def byteType: String = "Byte"

  override def dateType: String = "java.util.Date"

  override def timestampType: String = "java.sql.Timestamp"

  override def binaryType: String = generateArrayTypeExpression(byteType)

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s"java.util.List<$elementTypeExpression>"

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String =
    s"""java.util.Map<$keyExpression, $valueExpression>"""

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    classTypeMetaData.packageName + "." + classTypeMetaData.typeName

  override def toString: String = "JavaDialect"

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    s"""public class ${classTypeMetaData.typeName} {
       |${generatePrivateFieldsExpression(fieldsExpressions)}
       |${generateDefaultConstructor(classTypeMetaData)}
       |${generateConstructor(classTypeMetaData)}
       |${generateGettersAndSetters(classTypeMetaData)}
       |}""".stripMargin

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    s"   private $typeExpression ${f.fieldName};"

  private def generatePrivateFieldsExpression(fieldsExpressions: Iterable[String]): String =
    fieldsExpressions.mkString("\n")

  private def generateDefaultConstructor(classTypeMetaData: ClassTypeMetaData): String =
    s"\n   public ${classTypeMetaData.typeName}() {}"

  private def generateConstructor(classTypeMetaData: ClassTypeMetaData): String =
    s"""
       |   public ${classTypeMetaData.typeName}(${classTypeMetaData.fields.map(f => s"${generateTypeExpression(f.fieldType)} ${f.fieldName}").mkString(", ")}) {
       |      ${classTypeMetaData.fields.map(f => s"this.${f.fieldName} = ${f.fieldName};").mkString("\n      ")}
       |   }""".stripMargin

  private def generateGettersAndSetters(classTypeMetaData: ClassTypeMetaData): String =
    classTypeMetaData
      .fields
      .map(f =>
        s"""
           |   public ${generateTypeExpression(f.fieldType)} get${f.fieldName.capitalize}() {
           |      return ${f.fieldName};
           |   }
           |
           |   public void set${f.fieldName.capitalize}(${generateTypeExpression(f.fieldType)} ${f.fieldName}) {
           |      this.${f.fieldName} = ${f.fieldName};
           |   }""".stripMargin)
      .mkString("\n")
}
