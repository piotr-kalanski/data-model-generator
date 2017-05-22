package com.datawizards.dmg.examples

import java.sql.{Date, Timestamp}
import com.datawizards.dmg.annotations._

object TestModel {
  case class Person(name: String, age: Int)
  case class Book(title: String, year: Int, personName: String)
  case class ClassWithAllSimpleTypes(
    strVal: String,
    intVal: Int,
    longVal: Long,
    doubleVal: Double,
    floatVal: Float,
    shortVal: Short,
    booleanVal: Boolean,
    byteVal: Byte,
    dateVal: Date,
    timestampVal: Timestamp
  )
  case class PersonWithCustomName(
    @column(name="personName") name: String,
    @column(name="personAge") age: Int,
    gender: String
  )
}
