package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.CV
import com.datawizards.dmg.{DataModelGenerator, dialects}

object ArrayTypeExample extends App {
  println(DataModelGenerator.generate[CV](dialects.H2))
  println(DataModelGenerator.generate[CV](dialects.Hive))
  println(DataModelGenerator.generate[CV](dialects.ElasticsearchDialect))
  println(DataModelGenerator.generate[CV](dialects.AvroSchema))
  println(DataModelGenerator.generate[CV](dialects.AvroSchemaRegistryDialect))
}
