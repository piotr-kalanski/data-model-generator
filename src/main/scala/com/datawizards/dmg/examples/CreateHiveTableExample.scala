package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Person
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.service.HiveServiceImpl

object CreateHiveTableExample extends App {
  implicit val hiveGenerator = new HiveGenerator
  HiveServiceImpl.createHiveTable[Person]()
}
