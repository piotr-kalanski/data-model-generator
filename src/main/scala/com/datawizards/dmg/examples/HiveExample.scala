package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object HiveExample extends App {
  println(DataModelGenerator.generate[ParquetTableWithManyAnnotations](dialects.HiveDialect))
  println(DataModelGenerator.generate[AvroTableWithManyAnnotations](dialects.HiveDialect))
}
