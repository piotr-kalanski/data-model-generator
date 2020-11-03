package com.datawizards.dmg.generator

object DropAndCreateTable extends Enumeration {
  type DropAndCreateTable = Value
  val Always, Never, OnlyForExternalTables = Value
}
