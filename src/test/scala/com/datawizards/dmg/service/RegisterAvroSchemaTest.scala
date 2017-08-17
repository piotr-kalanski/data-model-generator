package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.Person
import com.datawizards.dmg.dialects.AvroSchemaRegistryDialect
import com.datawizards.dmg.repository.AvroSchemaRegistryRepository
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class RegisterAvroSchemaTest extends FunSuite {

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
    val subject = "person"
    service.registerSchema[Person](subject)
    service.subjects().exists(s => s == subject)
    assertResult(DataModelGenerator.generate[Person](AvroSchemaRegistryDialect)) {
      service.fetchSchema(subject)
    }
  }

}
