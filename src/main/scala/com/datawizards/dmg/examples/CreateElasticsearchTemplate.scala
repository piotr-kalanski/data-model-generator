package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.PersonWithMultipleEsAnnotations
import com.datawizards.dmg.service.ElasticsearchServiceImpl

object CreateElasticsearchTemplate extends App {
  val service = new ElasticsearchServiceImpl("http://localhost:9200")
  service.updateTemplate[PersonWithMultipleEsAnnotations]("people")

  println("Template:")
  println(service.getTemplate("people"))
}
