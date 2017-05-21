package com.datawizards.dmg.examples

import java.sql.{Date, Timestamp}

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
}
