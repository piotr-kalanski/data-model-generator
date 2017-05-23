package com.datawizards.dmg.examples

import com.datawizards.dmg.examples.TestModel.PersonWithComments
import com.datawizards.dmg.{DataModelGenerator, dialects}

object TableColumnCommentsExample extends App {
  println(DataModelGenerator.generate[PersonWithComments](dialects.H2))
  println(DataModelGenerator.generate[PersonWithComments](dialects.AvroSchema))
  println(DataModelGenerator.generate[PersonWithComments](dialects.Hive))
}
