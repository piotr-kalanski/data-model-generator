package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.{DataModelGenerator, dialects}

object HiveExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Hive))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.Hive))
}
