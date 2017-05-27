package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.ClassTypeMetaData
import com.datawizards.dmg.model._

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

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s"java.util.List<$elementTypeExpression>"

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    classTypeMetaData.packageName + "." + classTypeMetaData.typeName

  override def toString: String = "JavaDialect"

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    s"""public class ${classMetaData.className} {
       |${generatePrivateFieldsExpression(classMetaData)}
       |${generateDefaultConstructor(classMetaData)}
       |${generateConstructor(classMetaData)}
       |${generateGettersAndSetters(classMetaData)}
       |}""".stripMargin
  }

  private def generatePrivateFieldsExpression(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f => s"   private ${generateTypeExpression(f)} ${f.name};").mkString("\n")

  private def generateDefaultConstructor(classMetaData: ClassMetaData): String =
    s"\n   public ${classMetaData.className}() {}"

  private def generateConstructor(classMetaData: ClassMetaData): String =
    s"""
       |   public ${classMetaData.className}(${classMetaData.fields.map(f => s"${generateTypeExpression(f)} ${f.name}").mkString(", ")}) {
       |      ${classMetaData.fields.map(f => s"this.${f.name} = ${f.name};").mkString("\n      ")}
       |   }""".stripMargin

  private def generateGettersAndSetters(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f =>
        s"""
           |   public ${generateTypeExpression(f)} get${f.name.capitalize}() {
           |      return ${f.name};
           |   }
           |
           |   public void set${f.name.capitalize}(${generateTypeExpression(f)} ${f.name}) {
           |      this.${f.name} = ${f.name};
           |   }""".stripMargin)
      .mkString("\n")

}
