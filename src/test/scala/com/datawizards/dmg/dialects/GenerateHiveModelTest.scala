package com.datawizards.dmg.dialects

import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import com.datawizards.dmg.TestModel._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateHiveModelTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](HiveDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """CREATE TABLE ClassWithAllSimpleTypes(
        |   strVal STRING,
        |   intVal INT,
        |   longVal BIGINT,
        |   doubleVal DOUBLE,
        |   floatVal FLOAT,
        |   shortVal SMALLINT,
        |   booleanVal BOOLEAN,
        |   byteVal TINYINT,
        |   dateVal DATE,
        |   timestampVal TIMESTAMP
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](HiveDialect)
    }
  }

  test("Table and column comment") {
    val expected =
      """CREATE TABLE PersonWithComments(
        |   name STRING COMMENT 'Person name',
        |   age INT
        |)
        |COMMENT 'People data';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithComments](HiveDialect)
    }
  }

  test("Column length") {
    val expected =
      """CREATE TABLE PersonWithCustomLength(
        |   name STRING(1000),
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithCustomLength](HiveDialect)
    }
  }

  test("Array type") {
    val expected =
      """CREATE TABLE CV(
        |   skills ARRAY<STRING>,
        |   grades ARRAY<INT>
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](HiveDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """CREATE TABLE NestedArray(
        |   nested ARRAY<ARRAY<STRING>>,
        |   nested3 ARRAY<ARRAY<ARRAY<INT>>>
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](HiveDialect)
    }
  }

  test("Struct types") {
    val expected =
      """CREATE TABLE Book(
        |   title STRING,
        |   year INT,
        |   owner STRUCT<name : STRING, age : INT>,
        |   authors ARRAY<STRUCT<name : STRING, age : INT>>
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](HiveDialect)
    }
  }

  test("External table") {
    val expected =
      """CREATE EXTERNAL TABLE PersonExternalTable(
        |   name STRING,
        |   age INT
        |)
        |LOCATION 'hdfs:///data/people';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonExternalTable](HiveDialect)
    }
  }

  test("STORED AS PARQUET") {
    val expected =
      """CREATE TABLE PersonStoredAsParquet(
        |   name STRING,
        |   age INT
        |)
        |STORED AS PARQUET;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonStoredAsParquet](HiveDialect)
    }
  }

  test("STORED AS avro") {
    val expected =
      """CREATE TABLE PersonStoredAsAvro(
        |   name STRING,
        |   age INT
        |)
        |STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonStoredAsAvro](HiveDialect)
    }
  }

  test("ROW FORMAT SERDE") {
    val expected =
      """CREATE TABLE PersonRowFormatSerde(
        |   name STRING,
        |   age INT
        |)
        |ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonRowFormatSerde](HiveDialect)
    }
  }

  test("Multiple table properties") {
    val expected =
      """CREATE TABLE PersonMultipleTableProperties(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(
        |   'key1' = 'value1',
        |   'key2' = 'value2',
        |   'key3' = 'value3'
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonMultipleTableProperties](HiveDialect)
    }
  }

  test("Table properties - avro schema url") {
    val expected =
      """CREATE TABLE PersonAvroSchemaURL
        |TBLPROPERTIES(
        |   'avro.schema.url' = 'hdfs:///metadata/person.avro'
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonAvroSchemaURL](HiveDialect)
    }
  }

}
