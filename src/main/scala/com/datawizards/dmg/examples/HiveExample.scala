package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, dialects}

object HiveExample extends App {
  println(DataModelGenerator.generate[ParquetTableWithManyAnnotations](new HiveGenerator))
  println(DataModelGenerator.generate[AvroTableWithManyAnnotations](new HiveGenerator))
}
