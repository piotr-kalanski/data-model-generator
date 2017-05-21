package com.datawizards.dmg.dialects

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateAvroSchemaRegistryTest extends FunSuite {

  test("Simple model") {
    val expected =
      """{"schema":
        |"{
        |   \"namespace\": \"com.datawizards.dmg\",
        |   \"type\": \"record\",
        |   \"name\": \"Person\",
        |   \"fields\": [
        |      {\"name\": \"name\", \"type\": \"string\"},
        |      {\"name\": \"age\", \"type\": \"int\"}
        |   ]
        |}"
        |}""".stripMargin

    assertResult(expected) {
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
        |      {\"name\": \"strVal\", \"type\": \"string\"},
        |      {\"name\": \"intVal\", \"type\": \"int\"},
        |      {\"name\": \"longVal\", \"type\": \"long\"},
        |      {\"name\": \"doubleVal\", \"type\": \"double\"},
        |      {\"name\": \"floatVal\", \"type\": \"float\"},
        |      {\"name\": \"shortVal\", \"type\": \"bytes\"},
        |      {\"name\": \"booleanVal\", \"type\": \"boolean\"},
        |      {\"name\": \"byteVal\", \"type\": \"bytes\"},
        |      {\"name\": \"dateVal\", \"type\": \"int\"},
        |      {\"name\": \"timestampVal\", \"type\": \"long\"}
        |   ]
        |}"
        |}""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](AvroSchemaRegistryDialect)
    }
  }

}
