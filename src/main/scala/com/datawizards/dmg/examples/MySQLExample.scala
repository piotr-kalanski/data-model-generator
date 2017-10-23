package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object MySQLExample extends App {
  println(DataModelGenerator.generate[Person](dialects.MySQLDialect))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.MySQLDialect))
  println(DataModelGenerator.generate[PersonWithComments](dialects.MySQLDialect))
  println(DataModelGenerator.generate[CV](dialects.MySQLDialect))
}
