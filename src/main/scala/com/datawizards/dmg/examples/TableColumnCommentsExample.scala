package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.PersonWithComments
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

object TableColumnCommentsExample extends App {
  println(DataModelGenerator.generate[PersonWithComments](dialects.H2Dialect))
  println(DataModelGenerator.generate[PersonWithComments](dialects.AvroSchemaDialect))
  println(DataModelGenerator.generate[PersonWithComments](new HiveGenerator))
}
