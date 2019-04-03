package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Person
import com.datawizards.dmg.service.AvroSchemaRegistryServiceImpl

object CopyAvroSchemaToHDFS extends App {
  val service = new AvroSchemaRegistryServiceImpl("http://localhost:8081")
  service.copyAvroSchemaToHdfs[Person]("/metadata/schemas/person")
}