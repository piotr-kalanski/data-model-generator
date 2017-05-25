package com.datawizards.dmg.dialects

import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import com.datawizards.dmg.TestModel._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateAvroSchemaTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "Person",
        |   "fields": [
        |      {"name": "name", "type": "string"},
        |      {"name": "age", "type": "int"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](AvroSchemaDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "ClassWithAllSimpleTypes",
        |   "fields": [
        |      {"name": "strVal", "type": "string"},
        |      {"name": "intVal", "type": "int"},
        |      {"name": "longVal", "type": "long"},
        |      {"name": "doubleVal", "type": "double"},
        |      {"name": "floatVal", "type": "float"},
        |      {"name": "shortVal", "type": "bytes"},
        |      {"name": "booleanVal", "type": "boolean"},
        |      {"name": "byteVal", "type": "bytes"},
        |      {"name": "dateVal", "type": "int"},
        |      {"name": "timestampVal", "type": "long"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](AvroSchemaDialect)
    }
  }

  test("Table and column comment") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "PersonWithComments",
        |   "doc": "People data",
        |   "fields": [
        |      {"name": "name", "type": "string", "doc": "Person name"},
        |      {"name": "age", "type": "int"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithComments](AvroSchemaDialect)
    }
  }

  test("Array type") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "CV",
        |   "fields": [
        |      {"name": "skills", "type": "array", "items": "string"},
        |      {"name": "grades", "type": "array", "items": "int"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](AvroSchemaDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "NestedArray",
        |   "fields": [
        |      {"name": "nested", "type": "array", "items": {"type": "array", "items": "string"}},
        |      {"name": "nested3", "type": "array", "items": {"type": "array", "items": {"type": "array", "items": "int"}}}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](AvroSchemaDialect)
    }
  }

  test("Struct types") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "Book",
        |   "fields": [
        |      {"name": "title", "type": "string"},
        |      {"name": "year", "type": "int"},
        |      {"name": "owner", "type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]},
        |      {"name": "authors", "type": "array", "items": {"type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]}}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](AvroSchemaDialect)
    }
  }

}
