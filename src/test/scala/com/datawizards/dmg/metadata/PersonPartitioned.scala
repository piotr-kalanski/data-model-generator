package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive.{hiveExternalTable, hivePartitionColumn}
import com.datawizards.dmg.dialects._

@table("person_partitioned_hehehe")
@hiveExternalTable("s3://some/path")
case class PersonPartitioned(
  @column(name="personName_es", dialect = ElasticsearchDialect)
  @column(name="person_name")
  @anotherAnnotation("name2")
  name: String,
  @column(name="age_es", dialect = ElasticsearchDialect)
  age: Int,
  title: String,
  @hivePartitionColumn
  birthYear: Int,
  @hivePartitionColumn
  birthMonth: Int,
  @hivePartitionColumn
  birthDay: Int
)

