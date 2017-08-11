package com.datawizards.dmg.dialects

import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import com.datawizards.dmg.TestModel._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateElasticsearchMappingTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """{
        |   "mappings" : {
        |      "Person" : {
        |         "properties" : {
        |            "name" : {"type" : "string"},
        |            "age" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](ElasticsearchDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """{
        |   "mappings" : {
        |      "ClassWithAllSimpleTypes" : {
        |         "properties" : {
        |            "strVal" : {"type" : "string"},
        |            "intVal" : {"type" : "integer"},
        |            "longVal" : {"type" : "long"},
        |            "doubleVal" : {"type" : "double"},
        |            "floatVal" : {"type" : "float"},
        |            "shortVal" : {"type" : "short"},
        |            "booleanVal" : {"type" : "boolean"},
        |            "byteVal" : {"type" : "byte"},
        |            "dateVal" : {"type" : "date"},
        |            "timestampVal" : {"type" : "date"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](ElasticsearchDialect)
    }
  }

  test("Array type") {
    val expected =
      """{
        |   "mappings" : {
        |      "CV" : {
        |         "properties" : {
        |            "skills" : {"type" : "string"},
        |            "grades" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](ElasticsearchDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """{
        |   "mappings" : {
        |      "NestedArray" : {
        |         "properties" : {
        |            "nested" : {"type" : "string"},
        |            "nested3" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](ElasticsearchDialect)
    }
  }

  test("Struct types") {
    val expected =
      """{
        |   "mappings" : {
        |      "Book" : {
        |         "properties" : {
        |            "title" : {"type" : "string"},
        |            "year" : {"type" : "integer"},
        |            "owner" : {"properties" : {"name" : {"type" : "string"}, "age" : {"type" : "integer"}}},
        |            "authors" : {"properties" : {"name" : {"type" : "string"}, "age" : {"type" : "integer"}}}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](ElasticsearchDialect)
    }
  }

  test("index option") {
    val expected =
      """{
        |   "mappings" : {
        |      "PersonEsIndexSettings" : {
        |         "properties" : {
        |            "name" : {"type" : "string", "index" : "not_analyzed"},
        |            "age" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonEsIndexSettings](ElasticsearchDialect)
    }
  }

  test("format option") {
    val expected =
      """{
        |   "mappings" : {
        |      "PersonWithDateFormat" : {
        |         "properties" : {
        |            "name" : {"type" : "string"},
        |            "birthday" : {"type" : "date", "format" : "yyyy-MM-dd"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithDateFormat](ElasticsearchDialect)
    }
  }

  test("index settings") {
    val expected =
      """{
        |   "settings" : {
        |      "number_of_shards" : 1,
        |      "number_of_replicas" : 3,
        |      "blocks.read_only" : "true",
        |      "codec" : "best_compression"
        |   },
        |   "mappings" : {
        |      "PersonWithIndexSettings" : {
        |         "properties" : {
        |            "name" : {"type" : "string"},
        |            "age" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithIndexSettings](ElasticsearchDialect)
    }
  }

  test("Template") {
    val expected =
      """{
        |   "template" : "people*",
        |   "mappings" : {
        |      "PersonWithEsTemplate" : {
        |         "properties" : {
        |            "name" : {"type" : "string"},
        |            "age" : {"type" : "integer"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithEsTemplate](ElasticsearchDialect)
    }
  }

  test("Multiple options") {
    val expected =
      """{
        |   "template" : "people*",
        |   "settings" : {
        |      "number_of_shards" : 1,
        |      "number_of_replicas" : 3
        |   },
        |   "mappings" : {
        |      "people" : {
        |         "properties" : {
        |            "personName" : {"type" : "string", "index" : "not_analyzed"},
        |            "personBirthday" : {"type" : "date", "format" : "yyyy-MM-dd"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithMultipleEsAnnotations](ElasticsearchDialect)
    }
  }

  test("Map type") {
    val expected =
      """{
        |   "mappings" : {
        |      "ClassWithMap" : {
        |         "properties" : {
        |            "map" : {"type" : "string"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](ElasticsearchDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """{
        |   "mappings" : {
        |      "ClassWithArrayByte" : {
        |         "properties" : {
        |            "arr" : {"type" : "byte"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](ElasticsearchDialect)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """{
        |   "mappings" : {
        |      "ClassWithBigInteger" : {
        |         "properties" : {
        |            "n1" : {"type" : "double"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](ElasticsearchDialect)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """{
        |   "mappings" : {
        |      "ClassWithBigDecimal" : {
        |         "properties" : {
        |            "n1" : {"type" : "double"}
        |         }
        |      }
        |   }
        |}""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](ElasticsearchDialect)
    }
  }
}
