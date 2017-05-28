package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object ElasticsearchExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Elasticsearch))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.Elasticsearch))
  println(DataModelGenerator.generate[PersonWithMultipleEsAnnotations](dialects.Elasticsearch))
}
