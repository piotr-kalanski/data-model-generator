package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGeneratorBaseTest
import com.datawizards.dmg.TestModel.PersonWithPlaceholderVariables
import com.datawizards.dmg.repository.AvroSchemaRegistryRepository
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class AvroSchemaGenerateTest extends DataModelGeneratorBaseTest {
  val service: AvroSchemaRegistryService = new AvroSchemaRegistryService {
    override protected val repository: AvroSchemaRegistryRepository = new AvroSchemaRegistryRepository {

      private val schemas = mutable.Map[String, String]()

      override def fetchSchema(subject: String, version: String): String =
        schemas(subject)

      override def subjects(): Iterable[String] =
        schemas.keys

      override def registerSchema(subject: String, schema: String): Unit = {
        schemas += subject -> schema
      }
    }
    override protected val hdfsService:HDFSService = HDFSServiceImpl
  }

  test("Register and fetch avro schema") {
    val expected =
      """{
        |   "namespace": "com.datawizards.dmg",
        |   "type": "record",
        |   "name": "tttt",
        |   "fields": [
        |      {"name": "name", "type": ["null", "string"], "doc": "Person some comment"},
        |      {"name": "age", "type": ["null", "int"]}
        |   ]
        |}""".stripMargin

    assertResultIgnoringNewLines(expected){
      service.generateSchema[PersonWithPlaceholderVariables](Map("table_name" -> "tttt", "name_comment" -> "some comment"))
    }
  }
}
