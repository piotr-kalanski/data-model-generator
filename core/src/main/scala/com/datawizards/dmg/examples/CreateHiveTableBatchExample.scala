package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{Person, PersonWithCustomName}
import com.datawizards.dmg.service.HiveServiceImpl

object CreateHiveTableBatchExample extends App {
  HiveServiceImpl.batchCreateTable()
    .createTable[Person]
    .createTable[PersonWithCustomName]
    .execute()
}
