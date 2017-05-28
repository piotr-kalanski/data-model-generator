package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.Person
import com.datawizards.dmg.dialects.{AvroSchemaRegistryDialect, ElasticsearchDialect}
import com.datawizards.dmg.repository.ElasticsearchRepository
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class CreateElasticsearchIndexTest extends FunSuite {

  val service: ElasticsearchService = new ElasticsearchService {
    override protected val repository: ElasticsearchRepository = new ElasticsearchRepository {

      private val templates = mutable.Map[String, String]()
      private val indexes = mutable.Map[String, String]()

      override def updateTemplate(templateName: String, mapping: String): Unit =
        templates(templateName) = mapping

      override def getTemplate(templateName: String): String =
        templates(templateName)

      override def createIndex(indexName: String, mapping: String): Unit =
        indexes(indexName) = mapping

      override def getIndexSettings(indexName: String): String =
        indexes(indexName)
    }
  }

  test("Create and get Elasticsearch template") {
    val name = "person"
    service.updateTemplate[Person](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getTemplate(name)
    }
  }

  test("Create and get Elasticsearch index") {
    val name = "person"
    service.createIndex[Person](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getIndexSettings(name)
    }
  }

}
