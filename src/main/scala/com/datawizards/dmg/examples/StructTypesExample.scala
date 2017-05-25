package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Book
import com.datawizards.dmg.{DataModelGenerator, dialects}

object StructTypesExample extends App {
  println(DataModelGenerator.generate[Book](dialects.H2))
  println(DataModelGenerator.generate[Book](dialects.Hive))
  println(DataModelGenerator.generate[Book](dialects.Redshift))
  println(DataModelGenerator.generate[Book](dialects.AvroSchema))
  println(DataModelGenerator.generate[Book](dialects.Elasticsearch))
}
