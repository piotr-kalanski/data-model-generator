package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{Person, PersonWithCustomName}
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.service.HiveServiceImpl

object CreateHiveTableBatchExample extends App {
  implicit val hiveGenerator = new HiveGenerator
  HiveServiceImpl.batchCreateTable()
    .createTable[Person]
    .createTable[PersonWithCustomName]
    .execute()
}
