package com.datawizards.dmg.service

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.dialects._
import com.datawizards.esclient.repository.ElasticsearchRepository
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.universe

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

      override def deleteTemplate(templateName: String): Unit =
        templates.remove(templateName)

      override def templateExists(templateName: String): Boolean =
        templates.contains(templateName)

      override def deleteIndex(indexName: String): Unit =
        indexes.remove(indexName)

      override def indexExists(indexName: String): Boolean =
        indexes.contains(indexName)

      override def status(): Boolean = true

      override def index[T <: AnyRef](indexName: String, typeName: String, documentId: String, document: T): Unit =
        { /* do nothing */ }

      override def index(indexName: String, typeName: String, documentId: String, document: String): Unit =
        { /* do nothing */ }

      override def read[T](indexName: String, typeName: String, documentId: String)(implicit ct: ClassTag[T], tt: universe.TypeTag[T]): T = "".asInstanceOf[T]

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

  test("Try to create index if exists") {
    val name = "person"
    service.createIndex[Person](name)
    service.createIndexIfNotExists[Book](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getIndexSettings(name)
    }
  }

  test("Create index twice") {
    val name = "person"
    service.createIndex[Book](name)
    service.createIndex[Person](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getIndexSettings(name)
    }
  }

  test("Try to create template if exists") {
    val name = "person"
    service.updateTemplate[Person](name)
    service.updateTemplateIfNotExists[Book](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getTemplate(name)
    }
  }

  test("Create template twice") {
    val name = "person"
    service.updateTemplate[Book](name)
    service.updateTemplate[Person](name)

    assertResult(DataModelGenerator.generate[Person](ElasticsearchDialect)) {
      service.getTemplate(name)
    }
  }

}
