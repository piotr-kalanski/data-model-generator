package com.datawizards.dmg.examples

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.dmg.examples.TestModel.PersonWithMultipleCustomNames

object MultipleCustomNames extends App {
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.H2))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.Hive))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.Redshift))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.AvroSchema))
  println(DataModelGenerator.generate[PersonWithMultipleCustomNames](dialects.Elasticsearch))
}
