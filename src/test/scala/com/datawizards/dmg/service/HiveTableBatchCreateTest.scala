package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGeneratorBaseTest
import com.datawizards.dmg.TestModel.PersonWithPlaceholderVariables
import com.datawizards.dmg.examples.TestModel.{Person, PersonWithCustomName}
import com.datawizards.dmg.metadata.PersonPartitioned
import com.datawizards.dmg.repository.AvroSchemaRegistryRepository
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class HiveTableBatchCreateTest extends DataModelGeneratorBaseTest {
  test("Batch create hive table") {
    val expected =
      """DROP TABLE IF EXISTS Person;
        |CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |);
        |
        |
        |DROP TABLE IF EXISTS person_partitioned_hehehe;
        |CREATE EXTERNAL TABLE person_partitioned_hehehe(
        |   person_name STRING,
        |   age INT,
        |   title STRING
        |)
        |PARTITIONED BY(birthYear INT, birthMonth INT, birthDay INT)
        |LOCATION 's3://some/path';
        |MSCK REPAIR TABLE person_partitioned_hehehe;
        |
        |
        |DROP TABLE IF EXISTS PersonWithCustomName;
        |CREATE TABLE PersonWithCustomName(
        |   personName STRING,
        |   personAge INT,
        |   gender STRING
        |);
        |
        |
        |""".stripMargin

    val script = HiveServiceImpl.batchCreateTable()
      .createTable[Person]
      .createTable[PersonPartitioned]
      .createTable[PersonWithCustomName]
      .getScript

    assertResultIgnoringNewLines(expected){script}
  }
}
