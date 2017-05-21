package com.datawizards.dmg.dialects

object AvroSchemaRegistryDialect extends DecoratorDialect(AvroSchemaDialect) {
  protected def decorate(dataModel: String): String =
    s"""{"schema":
       |"${dataModel.replace("\"", "\\\"")}"
       |}""".stripMargin
}
