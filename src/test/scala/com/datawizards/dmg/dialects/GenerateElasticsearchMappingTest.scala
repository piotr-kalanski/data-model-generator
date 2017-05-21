package com.datawizards.dmg.dialects

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateElasticsearchMappingTest extends FunSuite {

  test("Simple model") {
    val expected =
      """{
        |   "mappings": {
        |      "Person": {
        |         "name": {"type": "string"},
        |         "age": {"type": "integer"}
        |      }
        |   }
        |}""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[Person](ElasticsearchDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """{
        |   "mappings": {
        |      "ClassWithAllSimpleTypes": {
        |         "strVal": {"type": "string"},
        |         "intVal": {"type": "integer"},
        |         "longVal": {"type": "long"},
        |         "doubleVal": {"type": "double"},
        |         "floatVal": {"type": "float"},
        |         "shortVal": {"type": "short"},
        |         "booleanVal": {"type": "boolean"},
        |         "byteVal": {"type": "byte"},
        |         "dateVal": {"type": "date"},
        |         "timestampVal": {"type": "date"}
        |      }
        |   }
        |}""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](ElasticsearchDialect)
    }
  }

}
