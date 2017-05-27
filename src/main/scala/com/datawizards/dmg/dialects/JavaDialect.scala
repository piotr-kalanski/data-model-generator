package com.datawizards.dmg.dialects

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

  override def arrayType: String = "java.util.List"

  override def structType: String = "N/A"

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
      .map(f => s"   private ${getFieldType(f.targetType)} ${f.name};").mkString("\n")

  private def generateDefaultConstructor(classMetaData: ClassMetaData): String =
    s"\n   public ${classMetaData.className}() {}"

  private def generateConstructor(classMetaData: ClassMetaData): String =
    s"""
       |   public ${classMetaData.className}(${classMetaData.fields.map(f => s"${getFieldType(f.targetType)} ${f.name}").mkString(", ")}) {
       |      ${classMetaData.fields.map(f => s"this.${f.name} = ${f.name};").mkString("\n      ")}
       |   }""".stripMargin

  private def generateGettersAndSetters(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f =>
        s"""
           |   public ${getFieldType(f.targetType)} get${f.name.capitalize}() {
           |      return ${f.name};
           |   }
           |
           |   public void set${f.name.capitalize}(${getFieldType(f.targetType)} ${f.name}) {
           |      this.${f.name} = ${f.name};
           |   }""".stripMargin)
      .mkString("\n")

  private def getFieldType(fieldType: FieldType): String = fieldType match {
    case p:PrimitiveFieldType => p.name
    case a:ArrayFieldType => s"$arrayType<${getFieldType(a.elementType)}>"
    case s:StructFieldType => s.typeName
  }
}
