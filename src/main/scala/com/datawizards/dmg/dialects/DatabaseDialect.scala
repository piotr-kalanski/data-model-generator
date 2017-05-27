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
      .withFilter(f => generateColumn(f))
      .map(f =>
        f.name + " " + generateTypeExpression(f) +
        (if(f.length.isEmpty) "" else s"(${f.length.get})") +
        fieldAdditionalExpressions(f)
      ).mkString(",\n   ") +
    "\n)"

  protected def generateColumn(field: FieldMetaData): Boolean = true

  protected def fieldAdditionalExpressions(f: FieldMetaData): String

  protected def additionalTableProperties(classMetaData: ClassMetaData): String

  protected def additionalTableExpressions(classMetaData: ClassMetaData): String

}
