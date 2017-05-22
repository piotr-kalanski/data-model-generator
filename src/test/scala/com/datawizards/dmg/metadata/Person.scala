package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations._

@table("PEOPLE")
case class Person(
  @column(name="personName_es", context="es")
  @column(name="person_name")
  @anotherAnnotation("name2")
  name: String,
  @column(name="age_es", context = "es")
  age: Int,
  title: String
)

