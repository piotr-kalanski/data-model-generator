package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.CV
import com.datawizards.dmg.{DataModelGenerator, dialects}

object ArrayTypeExample extends App {
  println(DataModelGenerator.generate[CV](dialects.H2Dialect))
  println(DataModelGenerator.generate[CV](dialects.HiveDialect))
  println(DataModelGenerator.generate[CV](dialects.ElasticsearchDialect))
  println(DataModelGenerator.generate[CV](dialects.AvroSchemaDialect))
  println(DataModelGenerator.generate[CV](dialects.AvroSchemaRegistryDialect))
}
