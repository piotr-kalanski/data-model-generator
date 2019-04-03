package com.datawizards.dmg.examples

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.dmg.examples.TestModel.{ClassWithAllSimpleTypes, Person}

object H2Example extends App {
  println(DataModelGenerator.generate[Person](dialects.H2Dialect))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.H2Dialect))
}
