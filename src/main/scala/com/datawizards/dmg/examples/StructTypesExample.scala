package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Book
import com.datawizards.dmg.{DataModelGenerator, dialects}

object StructTypesExample extends App {
  println(DataModelGenerator.generate[Book](dialects.H2Dialect))
  println(DataModelGenerator.generate[Book](dialects.HiveDialect))
  println(DataModelGenerator.generate[Book](dialects.RedshiftDialect))
  println(DataModelGenerator.generate[Book](dialects.AvroSchemaDialect))
  println(DataModelGenerator.generate[Book](dialects.ElasticsearchDialect))
}
