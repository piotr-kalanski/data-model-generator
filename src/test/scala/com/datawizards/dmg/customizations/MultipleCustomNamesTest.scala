package com.datawizards.dmg.customizations

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.dialects._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MultipleCustomNamesTest extends DataModelGeneratorBaseTest {

  test("H2") {
    val expected =
      """CREATE TABLE PEOPLE(
        |   NAME VARCHAR,
        |   AGE INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](H2Dialect)
    }
  }

  test("Hive") {
    val expected =
      """CREATE TABLE people(
        |   name STRING,
        |   personAge INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '2110356556')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](new HiveGenerator)
    }
  }

  test("Hive 2") {
    val expected ="""CREATE TABLE class_with_multiple_dialects(
                    |   some_column STRING COMMENT 'hive comment 2',
                    |   another_column INT COMMENT 'general comment 3'
                    |)COMMENT 'hive comment'
                    |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '-885999637')
                    |;"""
      .stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMultipleDialects](new HiveGenerator)
    }
  }

  test("MySQL 2") {
    val expected =
      """CREATE TABLE ClassWithMultipleDialects(
        |   someColumn VARCHAR(200) COMMENT 'mysql comment 2',
        |   anotherColumn INT NOT NULL COMMENT 'mysql comment 3'
        |)COMMENT = 'mysql comment';"""
      .stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMultipleDialects](MySQLDialect)
    }
  }

  test("DefaultUnderscore MySQL 2") {
    val expected =
      """CREATE TABLE default_underscore(
        |   some_column VARCHAR(123) NOT NULL COMMENT 'asdf'
        |);"""
        .stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[DefaultUnderscore](MySQLDialect)
    }
  }

  test("Redshift") {
    val expected =
      """CREATE TABLE people(
        |   name VARCHAR,
        |   personAge INTEGER
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](RedshiftDialect)
    }
  }

  test("Avro schema") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "people",
        |   "fields": [
        |      {"name": "name", "type": ["null", "string"]},
        |      {"name": "personAge", "type": ["null", "int"]}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](AvroSchemaDialect)
    }
  }

  test("Elasticsearch") {
    val expected =
      """{
        |   "mappings" : {
        |      "person" : {
        |         "properties" : {
        |            "personNameEs" : {"type" : "string"},
        |            "personAgeEs" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](ElasticsearchDialect)
    }
  }

}
