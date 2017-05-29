package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object MySQLExample extends App {
  println(DataModelGenerator.generate[Person](dialects.MySQL))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.MySQL))
  println(DataModelGenerator.generate[PersonWithComments](dialects.MySQL))
  println(DataModelGenerator.generate[CV](dialects.MySQL))
}
