package com.datawizards.dmg.examples

import java.sql.{Date, Timestamp}

import com.datawizards.dmg.annotations._
import com.datawizards.dmg.dialects

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
  @comment("People data")
  case class PersonWithComments(
    @comment("Person name") name: String,
    age: Int
  )
  @table("people")
  @table("PEOPLE", dialects.H2)
  @table("person", dialects.Elasticsearch)
  case class PersonWithMultipleCustomNames(
    @column("NAME", dialects.H2)
    @column("personNameEs", dialects.Elasticsearch)
    name: String,
    @column("personAge")
    @column("AGE", dialects.H2)
    @column("personAgeEs", dialects.Elasticsearch)
    age: Int
  )
  case class CV(skills: Seq[String])
}
