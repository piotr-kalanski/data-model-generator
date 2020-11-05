package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGeneratorBaseTest
import com.datawizards.dmg.examples.TestModel.{Person, PersonWithCustomName}
import com.datawizards.dmg.generator.{DropAndCreateTable, HiveGenerator}
import com.datawizards.dmg.metadata.PersonPartitioned
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class HiveTableBatchCreateTest extends DataModelGeneratorBaseTest {
  test("Batch create hive table - recreate only for external tables") {
    implicit val hiveGenerator = new HiveGenerator
    val expected =
      """CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-203337385')
        |;
        |
        |
        |DROP TABLE IF EXISTS person_partitioned_hehehe;
        |CREATE EXTERNAL TABLE person_partitioned_hehehe(
        |   person_name STRING,
        |   age INT,
        |   title STRING
        |)
        |PARTITIONED BY(birthYear INT, birthMonth INT, birthDay INT)
        |LOCATION 's3://some/path'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-538009580')
        |;
        |MSCK REPAIR TABLE person_partitioned_hehehe;
        |
        |CREATE TABLE PersonWithCustomName(
        |   personName STRING,
        |   personAge INT,
        |   gender STRING
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1126877536')
        |;
        |
        |""".stripMargin

    val script = HiveServiceImpl.batchCreateTable()
      .createTable[Person]
      .createTable[PersonPartitioned]
      .createTable[PersonWithCustomName]
      .getScript

    assertResultIgnoringNewLines(expected){script}
  }

  test("Batch create hive table - recreate always") {
    implicit val hiveGenerator = new HiveGenerator(dropAndCreateTable = DropAndCreateTable.Always)
    val expected =
      """DROP TABLE IF EXISTS Person;
        |CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-203337385')
        |;
        |
        |DROP TABLE IF EXISTS person_partitioned_hehehe;
        |CREATE EXTERNAL TABLE person_partitioned_hehehe(
        |   person_name STRING,
        |   age INT,
        |   title STRING
        |)
        |PARTITIONED BY(birthYear INT, birthMonth INT, birthDay INT)
        |LOCATION 's3://some/path'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-538009580')
        |;
        |MSCK REPAIR TABLE person_partitioned_hehehe;
        |
        |
        |DROP TABLE IF EXISTS PersonWithCustomName;
        |CREATE TABLE PersonWithCustomName(
        |   personName STRING,
        |   personAge INT,
        |   gender STRING
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1126877536')
        |;
        |
        |""".stripMargin

    val script = HiveServiceImpl.batchCreateTable()
      .createTable[Person]
      .createTable[PersonPartitioned]
      .createTable[PersonWithCustomName]
      .getScript

    assertResultIgnoringNewLines(expected){script}
  }

  test("Batch create hive table - never recreate tables") {
    implicit val hiveGenerator = new HiveGenerator(dropAndCreateTable = DropAndCreateTable.Never)
    val expected =
      """CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-203337385')
        |;
        |
        |CREATE EXTERNAL TABLE person_partitioned_hehehe(
        |   person_name STRING,
        |   age INT,
        |   title STRING
        |)
        |PARTITIONED BY(birthYear INT, birthMonth INT, birthDay INT)
        |LOCATION 's3://some/path'
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-538009580')
        |;
        |MSCK REPAIR TABLE person_partitioned_hehehe;
        |
        |CREATE TABLE PersonWithCustomName(
        |   personName STRING,
        |   personAge INT,
        |   gender STRING
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '1126877536')
        |;
        |""".stripMargin

    val script = HiveServiceImpl.batchCreateTable()
      .createTable[Person]
      .createTable[PersonPartitioned]
      .createTable[PersonWithCustomName]
      .getScript

    assertResultIgnoringNewLines(expected){script}
  }

}
