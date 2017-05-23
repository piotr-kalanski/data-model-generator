package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.ClassMetaData

trait DatabaseDialect extends Dialect {

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val columnsExpression = generateColumnsExpression(classMetaData)

    s"CREATE TABLE ${classMetaData.className}(\n" +
      s"   $columnsExpression" +
      s"\n)${additionalTableProperties(classMetaData)};${additionalTableExpressions(classMetaData)}"
  }

  private def generateColumnsExpression(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f =>
        f.name + " " + f.targetType +
        (if(f.length.isEmpty) "" else s"(${f.length.get})") +
        (if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'")
      ).mkString(",\n   ")

  protected def additionalTableProperties(classMetaData: ClassMetaData): String

  protected def additionalTableExpressions(classMetaData: ClassMetaData): String

}
