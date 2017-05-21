package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.{DataModelGenerator, dialects}

object ElasticsearchExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Elasticsearch))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.Elasticsearch))
}
