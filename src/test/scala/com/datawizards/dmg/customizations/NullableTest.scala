package com.datawizards.dmg.customizations

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.dialects._
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NullableTest extends DataModelGeneratorBaseTest {

  test("H2") {
    val expected =
      """CREATE TABLE PersonWithNull(
        |   name VARCHAR NOT NULL,
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](H2Dialect)
    }
  }

  test("Hive") {
    val expected =
      """CREATE TABLE PersonWithNull(
        |   name STRING,
        |   age INT
        |)
        |TBLPROPERTIES(   'MODEL_GENERATOR_METADATA_HASH' = '2114803015')
        |;""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](HiveDialect)
    }
  }

  test("Redshift") {
    val expected =
      """CREATE TABLE PersonWithNull(
        |   name VARCHAR NOT NULL,
        |   age INTEGER
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](RedshiftDialect)
    }
  }

  test("MySQL") {
    val expected =
      """CREATE TABLE PersonWithNull(
        |   name VARCHAR NOT NULL,
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](MySQLDialect)
    }
  }

  test("Avro schema") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "PersonWithNull",
        |   "fields": [
        |      {"name": "name", "type": "string"},
        |      {"name": "age", "type": ["null", "int"]}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](AvroSchemaDialect)
    }
  }

  test("Elasticsearch") {
    val expected =
      """{
        |   "mappings" : {
        |      "PersonWithNull" : {
        |         "properties" : {
        |            "name" : {"type" : "string"},
        |            "age" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithNull](ElasticsearchDialect)
    }
  }

}
