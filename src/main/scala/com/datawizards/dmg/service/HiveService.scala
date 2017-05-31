package com.datawizards.dmg.service

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

trait HiveService {

  /**
    * Generates data model for provided type and creates new Hive table.
    * Before creating table it drops table with the same name.
    */
  def createHiveTable[T: ClassTag: TypeTag](): Unit

  /**
    * Generates data model for provided type and creates new Hive table.
    */
  def createHiveTableIfNotExists[T: ClassTag: TypeTag](): Unit
}
