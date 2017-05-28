package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Person
import com.datawizards.dmg.service.ElasticsearchServiceImpl

object CreateElasticsearchIndex extends App {
  val service = new ElasticsearchServiceImpl("http://localhost:9200")
  service.createIndex[Person]("person")

  println("Index:")
  println(service.getIndexSettings("person"))
}
