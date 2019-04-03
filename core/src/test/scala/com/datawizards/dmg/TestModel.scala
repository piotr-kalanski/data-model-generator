package com.datawizards.dmg

import java.sql.Timestamp
import java.sql.Date

import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.annotations.es._

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
  @table("PEOPLE", dialects.H2Dialect)
  @table("person", dialects.ElasticsearchDialect)
  case class PersonWithMultipleCustomNames(
    @column("NAME", dialects.H2Dialect)
    @column("personNameEs", dialects.ElasticsearchDialect)
    name: String,
    @column("personAge")
    @column("AGE", dialects.H2Dialect)
    @column("personAgeEs", dialects.ElasticsearchDialect)
    age: Int
  )

  case class CV(skills: Seq[String], grades: Seq[Int])
  case class NestedArray(nested: Seq[Seq[String]], nested3: Seq[Seq[Seq[Int]]])
  case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

  @hiveExternalTable(location="hdfs:///data/people")
  case class PersonExternalTable(name: String, age: Int)

  @hiveStoredAs(format="PARQUET")
  case class PersonStoredAsParquet(name: String, age: Int)

  @hiveStoredAs(format="INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'")
  case class PersonStoredAsAvro(name: String, age: Int)

  @hiveRowFormatSerde(format="org.apache.hadoop.hive.serde2.avro.AvroSerDe")
  case class PersonRowFormatSerde(name: String, age: Int)

  @hiveTableProperty("key1", "value1")
  @hiveTableProperty("key2", "value2")
  @hiveTableProperty("key3", "value3")
  case class PersonMultipleTableProperties(name: String, age: Int)

  @hiveTableProperty("avro.schema.url", "hdfs:///metadata/person.avro")
  case class PersonAvroSchemaURL(name: String, age: Int)

  case class PersonWithNull(
    @notNull
    name: String,
    age: Int
  )

  case class ClicksPartitioned(
    time: Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn
    year: Int,
    @hivePartitionColumn
    month: Int,
    @hivePartitionColumn
    day: Int
  )

  case class ClicksPartitionedWithOrder(
    time: Timestamp,
    event: String,
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
  )

  @table("CUSTOM_TABLE_NAME")
  @comment("Table comment")
  @hiveStoredAs(format="PARQUET")
  @hiveExternalTable(location="hdfs:///data/table")
  @hiveTableProperty("key1", "value1")
  @hiveTableProperty("key2", "value2")
  @hiveTableProperty("key3", "value3")
  case class ParquetTableWithManyAnnotations(
    @column("eventTime")
    @comment("Event time")
    time: Timestamp,
    @comment("Event name")
    event: String,
    @comment("User id")
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
  )

  @table("CUSTOM_TABLE_NAME")
  @comment("Table comment")
  @hiveRowFormatSerde(format="org.apache.hadoop.hive.serde2.avro.AvroSerDe")
  @hiveStoredAs("INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'")
  @hiveExternalTable(location="hdfs:///data/table")
  @hiveTableProperty("avro.schema.url", "hdfs:///metadata/table.avro")
  @hiveTableProperty("key1", "value1")
  @hiveTableProperty("key2", "value2")
  @hiveTableProperty("key3", "value3")
  case class AvroTableWithManyAnnotations(
    @column("eventTime")
    @comment("Event time")
    time: Timestamp,
    @comment("Event name")
    event: String,
    @comment("User id")
    user: String,
    @hivePartitionColumn(order=3)
    day: Int,
    @hivePartitionColumn(order=1)
    year: Int,
    @hivePartitionColumn(order=2)
    month: Int
  )

  case class PersonEsIndexSettings(
    @esIndex("not_analyzed") name: String,
    age: Int
  )

  case class PersonWithDateFormat(
    name: String,
    @esFormat("yyyy-MM-dd") birthday: Date
  )

  @esSetting("number_of_shards", 1)
  @esSetting("number_of_replicas", 3)
  @esSetting("blocks.read_only", true)
  @esSetting("codec", "best_compression")
  case class PersonWithIndexSettings(name: String, age: Int)

  @esTemplate("people*")
  case class PersonWithEsTemplate(name: String, age: Int)

  @table("people")
  @esTemplate("people*")
  @esSetting("number_of_shards", 1)
  @esSetting("number_of_replicas", 3)
  case class PersonWithMultipleEsAnnotations(
    @esIndex("not_analyzed")
    @column("personName")
    name: String,
    @column("personBirthday")
    @esFormat("yyyy-MM-dd")
    birthday: Date
  )

  @underscore(dialect = dialects.H2Dialect)
  case class PersonWithUnderscore(
    personName: String,
    personAge: Int
  )

  @underscore(dialect = dialects.H2Dialect)
  @underscore(dialect = dialects.HiveDialect)
  @underscore(dialect = dialects.RedshiftDialect)
  @table("PEOPLE", dialect = dialects.HiveDialect)
  case class PersonWithUnderscoreWithMultipleNames(
    @column("name", dialect = dialects.HiveDialect)
    @column("name", dialect = dialects.RedshiftDialect)
    personName: String,
    @column("age", dialect = dialects.RedshiftDialect)
    personAge: Int
  )

  @table("${table_name}")
  case class PersonWithPlaceholderVariables(
    @comment("Person ${name_comment}") name: String,
    age: Int
  )

  case class ClassWithMap(map: Map[Int, Boolean])
  case class ClassWithDash(`add-id`: String)
  case class ClassWithReservedKeywords(select: String, where: String)
  case class ClassWithArrayByte(arr: Array[Byte])
  case class ClassWithBigInteger(n1: BigInt)
  case class ClassWithBigDecimal(n1: BigDecimal)


  @comment(value="mysql comment", dialect=dialects.MySQLDialect)
  @comment(value="hive comment", dialect=dialects.HiveDialect)
  @underscore(dialect=dialects.HiveDialect)
  case class ClassWithMultipleDialects(
                                      @comment(value="mysql comment 2", dialect=dialects.MySQLDialect)
                                      @comment(value="hive comment 2", dialect=dialects.HiveDialect)
                                      @length(value=200, dialect=dialects.MySQLDialect)
                                      someColumn: String,

                                      @comment(value="mysql comment 3", dialect=dialects.MySQLDialect)
                                      @comment(value="general comment 3")
                                      @notNull(dialect=dialects.MySQLDialect)
                                      anotherColumn: Integer
                                      )

  @underscore
  case class DefaultUnderscore(
                              @length(123)
                              @notNull
                              @comment("asdf")
                              someColumn: String
                              )
}
