package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.Person
import com.datawizards.dmg.service.HiveServiceImpl

object CreateHiveTableExample extends App {
  HiveServiceImpl.createHiveTable[Person]()
  HiveServiceImpl.createHiveTableIfNotExists[Person]()
}
