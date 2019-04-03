package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations._
import com.datawizards.dmg.dialects._

@table("PEOPLE")
case class Person(
  @column(name="personName_es", dialect = ElasticsearchDialect)
  @column(name="person_name")
  @anotherAnnotation("name2")
  name: String,
  @column(name="age_es", dialect = ElasticsearchDialect)
  age: Int,
  title: String
)

