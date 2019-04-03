package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object RedshiftExample extends App {
  println(DataModelGenerator.generate[Person](dialects.RedshiftDialect))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.RedshiftDialect))
  println(DataModelGenerator.generate[PersonWithComments](dialects.RedshiftDialect))
  println(DataModelGenerator.generate[CV](dialects.RedshiftDialect))
}
