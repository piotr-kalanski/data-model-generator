package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Person
import com.datawizards.dmg.service.AvroSchemaRegistryServiceImpl

object RegisterAvroSchema extends App {
  val service = new AvroSchemaRegistryServiceImpl("http://localhost:8081")
  service.registerSchema[Person]("person")

  println("Subjects:")
  println(service.subjects())

  println("Registered schema:")
  println(service.fetchSchema("person"))
}
