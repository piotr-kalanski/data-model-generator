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

  test("Paritioned by") {
    val expected =
      """CREATE TABLE ClicksPartitioned(
        |   time TIMESTAMP,
        |   event STRING,
        |   `user` STRING
        |)
        |PARTITIONED BY(year INT, month INT, day INT);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClicksPartitioned](HiveDialect)
    }
  }

  test("Paritioned by - order") {
    val expected =
      """CREATE TABLE ClicksPartitionedWithOrder(
        |   time TIMESTAMP,
        |   event STRING,
        |   `user` STRING
        |)
        |PARTITIONED BY(year INT, month INT, day INT);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClicksPartitionedWithOrder](HiveDialect)
    }
  }

  test("ParquetTableWithManyAnnotations") {
    val expected =
      """CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME(
        |   eventTime TIMESTAMP COMMENT 'Event time',
        |   event STRING COMMENT 'Event name',
        |   `user` STRING COMMENT 'User id'
        |)
        |COMMENT 'Table comment'
        |PARTITIONED BY(year INT, month INT, day INT)
        |STORED AS PARQUET
        |LOCATION 'hdfs:///data/table'
        |TBLPROPERTIES(
        |   'key1' = 'value1',
        |   'key2' = 'value2',
        |   'key3' = 'value3'
        |);
        |MSCK REPAIR TABLE CUSTOM_TABLE_NAME;""".stripMargin

    println(expected)
    println(DataModelGenerator.generate[ParquetTableWithManyAnnotations](HiveDialect))

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ParquetTableWithManyAnnotations](HiveDialect)
    }
  }

  test("AvroTableWithManyAnnotations") {
    val expected =
      """CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME
        |COMMENT 'Table comment'
        |PARTITIONED BY(year INT, month INT, day INT)
        |ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
        |STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
        |LOCATION 'hdfs:///data/table'
        |TBLPROPERTIES(
        |   'avro.schema.url' = 'hdfs:///metadata/table.avro',
        |   'key1' = 'value1',
        |   'key2' = 'value2',
        |   'key3' = 'value3'
        |);
        |MSCK REPAIR TABLE CUSTOM_TABLE_NAME;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[AvroTableWithManyAnnotations](HiveDialect)
    }
  }

  test("Map type") {
    val expected =
      """CREATE TABLE ClassWithMap(
        |   map MAP<INT, BOOLEAN>
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](HiveDialect)
    }
  }

  test("ClassWithDash") {
    val expected =
      """CREATE TABLE ClassWithDash(
        |   `add-id` STRING
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithDash](HiveDialect)
    }
  }

  test("reserverd keywords") {
    val expected =
      """CREATE TABLE ClassWithReservedKeywords(
        |   `select` STRING,
        |   `where` STRING
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithReservedKeywords](HiveDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """CREATE TABLE ClassWithArrayByte(
        |   arr BINARY
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](HiveDialect)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """CREATE TABLE ClassWithBigInteger(
        |   n1 BIGINT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](HiveDialect)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """CREATE TABLE ClassWithBigDecimal(
        |   n1 DECIMAL(38,18)
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](HiveDialect)
    }
  }

}
