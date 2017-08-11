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
        |      {"name": "name", "type": ["null", "string"]},
        |      {"name": "age", "type": ["null", "int"]}
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
        |      {"name": "strVal", "type": ["null", "string"]},
        |      {"name": "intVal", "type": ["null", "int"]},
        |      {"name": "longVal", "type": ["null", "long"]},
        |      {"name": "doubleVal", "type": ["null", "double"]},
        |      {"name": "floatVal", "type": ["null", "float"]},
        |      {"name": "shortVal", "type": ["null", "bytes"]},
        |      {"name": "booleanVal", "type": ["null", "boolean"]},
        |      {"name": "byteVal", "type": ["null", "bytes"]},
        |      {"name": "dateVal", "type": ["null", "int"]},
        |      {"name": "timestampVal", "type": ["null", "long"]}
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
        |      {"name": "name", "type": ["null", "string"], "doc": "Person name"},
        |      {"name": "age", "type": ["null", "int"]}
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
        |      {"name": "title", "type": ["null", "string"]},
        |      {"name": "year", "type": ["null", "int"]},
        |      {"name": "owner", "type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]},
        |      {"name": "authors", "type": "array", "items": {"type": "record", "fields": [{"name": "name", "type": "string"}, {"name": "age", "type": "int"}]}}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](AvroSchemaDialect)
    }
  }

  test("Map type") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "ClassWithMap",
        |   "fields": [
        |      {"name": "map", "type": "map", "values": "boolean"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](AvroSchemaDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "ClassWithArrayByte",
        |   "fields": [
        |      {"name": "arr", "type": "array", "items": "bytes"}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](AvroSchemaDialect)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "ClassWithBigInteger",
        |   "fields": [
        |      {"name": "n1", "type": ["null", "double"]}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](AvroSchemaDialect)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "ClassWithBigDecimal",
        |   "fields": [
        |      {"name": "n1", "type": ["null", "double"]}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](AvroSchemaDialect)
    }
  }
}
