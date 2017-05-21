package com.datawizards.dmg

import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.dialects.AvroSchemaDialect
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateAvroSchemaTest extends FunSuite {

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

    assertResult(expected) {
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

    assertResult(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](AvroSchemaDialect)
    }
  }

}
