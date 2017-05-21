package com.datawizards.dmg.dialects

trait DatabaseDialect extends Dialect {

  override def generateDataModel(table: String, fields: Array[FieldWithType]): String = {
    val columnsExpressions = fields.map(f => f.field + " " + f.targetType).mkString(",\n   ")

    s"CREATE TABLE $table(\n" +
      s"   $columnsExpressions" +
      s"\n);"
  }

}
