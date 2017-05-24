package com.datawizards.dmg

import java.sql.Timestamp
import java.sql.Date

import com.datawizards.dmg.annotations._

object TestModel {
  case class Person(name: String, age: Int)
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

  case class PersonWithCustomLength(
    @length(1000) name: String,
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

  case class CV(skills: Seq[String], grades: Seq[Int])
  case class NestedArray(nested: Seq[Seq[String]], nested3: Seq[Seq[Seq[Int]]])
  case class Book(title: String, year: Int, authors:Seq[Person])
}
