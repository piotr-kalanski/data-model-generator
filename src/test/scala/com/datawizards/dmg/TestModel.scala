package com.datawizards.dmg

import java.sql.Timestamp
import java.sql.Date

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

  @table("PEOPLE")
  case class PersonWithCustomName(
    @column("personName") name: String,
    age: Int
  )

  @comment("People data")
  case class PersonWithComments(
    @comment("Person name") name: String,
    age: Int
  )

}
