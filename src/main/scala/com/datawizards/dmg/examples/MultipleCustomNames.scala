package com.datawizards.dmg.examples

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.dmg.examples.TestModel.PersonWithMultipleCustomNames
import com.datawizards.dmg.generator.HiveGenerator

object MultipleCustomNames extends App {
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.H2Dialect))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](new HiveGenerator))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.RedshiftDialect))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.AvroSchemaDialect))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.ElasticsearchDialect))
}
