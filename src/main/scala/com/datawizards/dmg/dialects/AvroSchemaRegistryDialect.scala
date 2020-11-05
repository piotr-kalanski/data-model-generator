package com.datawizards.dmg.dialects

import com.datawizards.dmg.generator.DecoratorGenerator

object AvroSchemaRegistryDialect extends DecoratorGenerator(AvroSchemaDialect) with Dialect {

  def getSupportedDialect(): Dialect = this

  protected def decorate(dataModel: String): String =
    s"""{"schema":
       |"${dataModel.replace("\"", "\\\"")}"
       |}""".stripMargin
}
