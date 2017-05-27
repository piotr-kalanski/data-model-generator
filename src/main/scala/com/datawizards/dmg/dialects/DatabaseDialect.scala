package com.datawizards.dmg.dialects

import com.datawizards.dmg.model._

trait DatabaseDialect extends Dialect {

  override def generateDataModel(classMetaData: ClassMetaData): String =
    createTableExpression(classMetaData) +
    generateColumnsExpression(classMetaData) +
    additionalTableProperties(classMetaData) + ";" +
    additionalTableExpressions(classMetaData)

  protected def createTableExpression(classMetaData: ClassMetaData): String =
    s"CREATE TABLE ${classMetaData.className}"

  protected def generateColumnsExpression(classMetaData: ClassMetaData): String =
    "(\n   " +
    classMetaData
      .fields
      .map(f =>
        f.name + " " + getFieldType(f.targetType) +
        (if(f.length.isEmpty) "" else s"(${f.length.get})") +
        fieldAdditionalExpressions(f)
      ).mkString(",\n   ") +
    "\n)"

  protected def fieldAdditionalExpressions(f: FieldMetaData): String

  protected def additionalTableProperties(classMetaData: ClassMetaData): String

  protected def additionalTableExpressions(classMetaData: ClassMetaData): String

  protected def getFieldType(fieldType: FieldType): String = fieldType match {
    case p:PrimitiveFieldType => p.name
    case a:ArrayFieldType => getArrayType(a)
    case s:StructFieldType => getStructType(s)
  }

  protected def getArrayType(a: ArrayFieldType): String

  protected def getStructType(s: StructFieldType): String

}
