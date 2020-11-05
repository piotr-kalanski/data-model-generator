package com.datawizards.dmg.dialects

import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.generator.HiveGenerator
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateHiveModelTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '877255039')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](new HiveGenerator)
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
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1365831312')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](new HiveGenerator)
    }
  }

  test("Table and column comment") {
    val expected =
      """CREATE TABLE PersonWithComments(
        |   name STRING COMMENT 'Person name',
        |   age INT
        |)
        |COMMENT 'People data'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1417254351')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithComments](new HiveGenerator)
    }
  }

  test("Column length") {
    val expected =
      """CREATE TABLE PersonWithCustomLength(
        |   name STRING(1000),
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1216179897')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithCustomLength](new HiveGenerator)
    }
  }

  test("Array type") {
    val expected =
      """CREATE TABLE CV(
        |   skills ARRAY<STRING>,
        |   grades ARRAY<INT>
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1979412102')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](new HiveGenerator)
    }
  }

  test("Nested array type") {
    val expected =
      """CREATE TABLE NestedArray(
        |   nested ARRAY<ARRAY<STRING>>,
        |   nested3 ARRAY<ARRAY<ARRAY<INT>>>
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1567724307')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](new HiveGenerator)
    }
  }

  test("Struct types") {
    val expected =
      """CREATE TABLE Book(
        |   title STRING,
        |   year INT,
        |   owner STRUCT<name : STRING, age : INT>,
        |   authors ARRAY<STRUCT<name : STRING, age : INT>>
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1808548213')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](new HiveGenerator)
    }
  }

  test("External table") {
    val expected =
      """DROP TABLE IF EXISTS PersonExternalTable;
        |CREATE EXTERNAL TABLE PersonExternalTable(
        |   name STRING,
        |   age INT
        |)
        |LOCATION 'hdfs:///data/people'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1960062525')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonExternalTable](new HiveGenerator)
    }
  }

  test("STORED AS PARQUET") {
    val expected =
      """CREATE TABLE PersonStoredAsParquet(
        |   name STRING,
        |   age INT
        |)
        |STORED AS PARQUET
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1929236886')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonStoredAsParquet](new HiveGenerator)
    }
  }

  test("STORED AS avro") {
    val expected =
      """CREATE TABLE PersonStoredAsAvro(
        |   name STRING,
        |   age INT
        |)
        |STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-783212951')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonStoredAsAvro](new HiveGenerator)
    }
  }

  test("ROW FORMAT SERDE") {
    val expected =
      """CREATE TABLE PersonRowFormatSerde(
        |   name STRING,
        |   age INT
        |)
        |ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1622708839')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonRowFormatSerde](new HiveGenerator)
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
        |   'key3' = 'value3',
        |   'MODEL_GENERATOR_METADATA_HASH' = '-1234842788'
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonMultipleTableProperties](new HiveGenerator)
    }
  }

  test("Table properties - avro schema url") {
    val expected =
      """CREATE TABLE PersonAvroSchemaURL
        |TBLPROPERTIES(
        |   'avro.schema.url' = 'hdfs:///metadata/person.avro',
        |   'MODEL_GENERATOR_METADATA_HASH' = '562021204'
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonAvroSchemaURL](new HiveGenerator)
    }
  }

  test("Paritioned by") {
    val expected =
      """CREATE TABLE ClicksPartitioned(
        |   time TIMESTAMP,
        |   event STRING,
        |   `user` STRING
        |)
        |PARTITIONED BY(year INT, month INT, day INT)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '2114508592')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClicksPartitioned](new HiveGenerator)
    }
  }

  test("Paritioned by - order") {
    val expected =
      """CREATE TABLE ClicksPartitionedWithOrder(
        |   time TIMESTAMP,
        |   event STRING,
        |   `user` STRING
        |)
        |PARTITIONED BY(year INT, month INT, day INT)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '505990335')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClicksPartitionedWithOrder](new HiveGenerator)
    }
  }

  test("ParquetTableWithManyAnnotations") {
    val expected =
      """DROP TABLE IF EXISTS CUSTOM_TABLE_NAME;
        |CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME(
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
        |   'key3' = 'value3',
        |   'MODEL_GENERATOR_METADATA_HASH' = '1664459034'
        |);
        |MSCK REPAIR TABLE CUSTOM_TABLE_NAME;""".stripMargin

    println(expected)
    println(DataModelGenerator.generate[ParquetTableWithManyAnnotations](new HiveGenerator))

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ParquetTableWithManyAnnotations](new HiveGenerator)
    }
  }

  test("AvroTableWithManyAnnotations") {
    val expected =
      """DROP TABLE IF EXISTS CUSTOM_TABLE_NAME;
        |CREATE EXTERNAL TABLE CUSTOM_TABLE_NAME
        |COMMENT 'Table comment'
        |PARTITIONED BY(year INT, month INT, day INT)
        |ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
        |STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
        |LOCATION 'hdfs:///data/table'
        |TBLPROPERTIES(
        |   'avro.schema.url' = 'hdfs:///metadata/table.avro',
        |   'key1' = 'value1',
        |   'key2' = 'value2',
        |   'key3' = 'value3',
        |   'MODEL_GENERATOR_METADATA_HASH' = '-1271021230'
        |);
        |MSCK REPAIR TABLE CUSTOM_TABLE_NAME;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[AvroTableWithManyAnnotations](new HiveGenerator)
    }
  }

  test("Map type") {
    val expected =
      """CREATE TABLE ClassWithMap(
        |   map MAP<INT, BOOLEAN>
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1678449248')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](new HiveGenerator)
    }
  }

  test("ClassWithDash") {
    val expected =
      """CREATE TABLE ClassWithDash(
        |   `add-id` STRING
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-81720190')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithDash](new HiveGenerator)
    }
  }

  test("reserverd keywords") {
    val expected =
      """CREATE TABLE ClassWithReservedKeywords(
        |   `select` STRING,
        |   `where` STRING
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1541439265')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithReservedKeywords](new HiveGenerator)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """CREATE TABLE ClassWithArrayByte(
        |   arr BINARY
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-639278055')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](new HiveGenerator)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """CREATE TABLE ClassWithBigInteger(
        |   n1 BIGINT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-1893068753')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](new HiveGenerator)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """CREATE TABLE ClassWithBigDecimal(
        |   n1 DECIMAL(38,18)
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '482231622')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](new HiveGenerator)
    }
  }

}
