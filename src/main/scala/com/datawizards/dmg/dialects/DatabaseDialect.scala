package com.datawizards.dmg.dialects

import com.datawizards.dmg.ClassMetaData

trait DatabaseDialect extends Dialect {

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val columnsExpression = generateColumnsExpression(classMetaData)

    s"CREATE TABLE ${classMetaData.className}(\n" +
      s"   $columnsExpression" +
      s"\n);"
  }

  private def generateColumnsExpression(classMetaData: ClassMetaData): String =
    classMetaData.fields.map(f => f.name + " " + f.targetType).mkString(",\n   ")

}
