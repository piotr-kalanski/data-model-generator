package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel._
import com.datawizards.dmg.{DataModelGenerator, dialects}

object RedshiftExample extends App {
  println(DataModelGenerator.generate[Person](dialects.Redshift))
  println(DataModelGenerator.generate[ClassWithAllSimpleTypes](dialects.Redshift))
  println(DataModelGenerator.generate[PersonWithComments](dialects.Redshift))
  println(DataModelGenerator.generate[CV](dialects.Redshift))
}
