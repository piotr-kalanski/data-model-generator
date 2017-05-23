package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.PersonWithCustomName
import com.datawizards.dmg.{DataModelGenerator, dialects}

object CustomTableColumnNameExample extends App {
  println(DataModelGenerator.generate[PersonWithCustomName](dialects.H2))
}
