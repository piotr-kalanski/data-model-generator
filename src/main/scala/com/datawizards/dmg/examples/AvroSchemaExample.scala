package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.{DataModelGenerator, dialects}

object AvroSchemaExample extends App {
  println(DataModelGenerator.generate[Person](dialects.AvroSchema))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.AvroSchema))

  println(DataModelGenerator.generate[Person](dialects.AvroSchemaRegistry))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.AvroSchemaRegistry))
}
