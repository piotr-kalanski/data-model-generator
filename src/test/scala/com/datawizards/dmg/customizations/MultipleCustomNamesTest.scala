package com.datawizards.dmg.customizations

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.dialects._
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
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleCustomNames](HiveDialect)
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
