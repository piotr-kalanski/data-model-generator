package com.datawizards.dmg.dialects

import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateAvroSchemaRegistryTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """{"schema":
        |"{
        |   \"namespace\": \"com.datawizards.dmg\",
        |   \"type\": \"record\",
        |   \"name\": \"Person\",
        |   \"fields\": [
        |      {\"name\": \"name\", \"type\": [\"null\", \"string\"]},
        |      {\"name\": \"age\", \"type\": [\"null\", \"int\"]}
        |   ]
        |}"
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](AvroSchemaRegistryDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """{"schema":
        |"{
        |   \"namespace\": \"com.datawizards.dmg\",
        |   \"type\": \"record\",
        |   \"name\": \"ClassWithAllSimpleTypes\",
        |   \"fields\": [
        |      {\"name\": \"strVal\", \"type\": [\"null\", \"string\"]},
        |      {\"name\": \"intVal\", \"type\": [\"null\", \"int\"]},
        |      {\"name\": \"longVal\", \"type\": [\"null\", \"long\"]},
        |      {\"name\": \"doubleVal\", \"type\": [\"null\", \"double\"]},
        |      {\"name\": \"floatVal\", \"type\": [\"null\", \"float\"]},
        |      {\"name\": \"shortVal\", \"type\": [\"null\", \"bytes\"]},
        |      {\"name\": \"booleanVal\", \"type\": [\"null\", \"boolean\"]},
        |      {\"name\": \"byteVal\", \"type\": [\"null\", \"bytes\"]},
        |      {\"name\": \"dateVal\", \"type\": [\"null\", \"int\"]},
        |      {\"name\": \"timestampVal\", \"type\": [\"null\", \"long\"]}
        |   ]
        |}"
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](AvroSchemaRegistryDialect)
    }
  }

}
