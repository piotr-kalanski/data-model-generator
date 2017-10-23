package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations.column
import com.datawizards.dmg.dialects._

case class PersonFull(
  @column("person_name")
  @column("personName", ElasticsearchDialect)
  name: Option[String] = None
  ,
  @column("person_age")
  @column("personAge", ElasticsearchDialect)
  age: Option[Int] = None
  ,
  @column("personTitle", ElasticsearchDialect)
  title: Option[String] = None
)
