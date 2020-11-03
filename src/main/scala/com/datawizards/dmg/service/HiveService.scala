package com.datawizards.dmg.service

import com.datawizards.dmg.generator.HiveGenerator

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

trait HiveService {

  /**
    * Generates data model for provided type and creates new Hive table.
    * Before creating table it drops table with the same name.
    */
  def createHiveTable[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty)(implicit hiveGenerator: HiveGenerator): Unit
}
