package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.PersonWithCustomName
import com.datawizards.dmg.{DataModelGenerator, dialects}

object CustomColumnNameExample extends App {
  println(DataModelGenerator.generate[PersonWithCustomName](dialects.H2))
}
