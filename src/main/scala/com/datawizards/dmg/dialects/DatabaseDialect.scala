package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

trait DatabaseDialect extends Dialect {

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    createTableExpression(classTypeMetaData) +
    generateColumnsExpression(classTypeMetaData, fieldsExpressions) +
    additionalTableProperties(classTypeMetaData) + ";" +
    additionalTableExpressions(classTypeMetaData)

  protected def createTableExpression(classTypeMetaData: ClassTypeMetaData): String =
    s"CREATE TABLE ${classTypeMetaData.typeName}"

  protected def generateColumnsExpression(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    "(\n   " + fieldsExpressions.mkString(",\n   ") + "\n)"

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    f.fieldName + " " + typeExpression +
      (if(fieldLength(f).isEmpty) "" else s"(${fieldLength(f).get})") +
      fieldAdditionalExpressions(f)

  protected def fieldAdditionalExpressions(f: ClassFieldMetaData): String

  protected def additionalTableProperties(classTypeMetaData: ClassTypeMetaData): String

  protected def additionalTableExpressions(classTypeMetaData: ClassTypeMetaData): String

}
