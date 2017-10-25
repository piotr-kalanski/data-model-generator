package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations.{column, table}
import com.datawizards.dmg.dialects._

@table("person_full")
@table("person_full_es", ElasticsearchDialect)
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
