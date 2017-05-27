package com.datawizards.dmg.examples

import java.sql.{Date, Timestamp}

import com.datawizards.dmg.annotations._
import com.datawizards.dmg.annotations.hive._
import com.datawizards.dmg.dialects

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
  case class Book(title: String, year: Int, owner: Person, authors: Seq[Person])

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
}
