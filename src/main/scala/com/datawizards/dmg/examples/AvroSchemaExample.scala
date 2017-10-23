package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.{DataModelGenerator, dialects}

object AvroSchemaExample extends App {
  println(DataModelGenerator.generate[Person](dialects.AvroSchemaDialect))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.AvroSchemaDialect))

  println(DataModelGenerator.generate[Person](dialects.AvroSchemaRegistryDialect))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.AvroSchemaRegistryDialect))
}
