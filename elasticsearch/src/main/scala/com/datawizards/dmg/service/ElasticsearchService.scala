package com.datawizards.dmg.service

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.esclient.repository.ElasticsearchRepository
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

trait ElasticsearchService {
  private val log = Logger.getLogger(getClass.getName)
  protected val repository: ElasticsearchRepository

  def updateTemplate[T: ClassTag: TypeTag](templateName: String, variables: Map[String, String] = Map.empty): Unit = {
    val template = TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.ElasticsearchDialect), variables)
    repository.deleteTemplate(templateName)
    repository.updateTemplate(templateName, template)
    log.info(s"Updated template [$templateName] at Elasticsearch.")
  }

  def updateTemplateIfNotExists[T: ClassTag: TypeTag](templateName: String, variables: Map[String, String] = Map.empty): Unit = {
    val template = TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.ElasticsearchDialect), variables)
    if(!repository.templateExists(templateName)) {
      repository.updateTemplate(templateName, template)
      log.info(s"Updated template [$templateName] at Elasticsearch.")
    }
  }

  def getTemplate(templateName: String): String =
    repository.getTemplate(templateName)

  def createIndexIfNotExists[T: ClassTag: TypeTag](indexName: String, variables: Map[String, String] = Map.empty): Unit = {
    val mapping = TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.ElasticsearchDialect), variables)
    if(!repository.indexExists(indexName)) {
      repository.createIndex(indexName, mapping)
      log.info(s"Created index [$indexName] at Elasticsearch.")
    }
  }

  def createIndex[T: ClassTag: TypeTag](indexName: String, variables: Map[String, String] = Map.empty): Unit = {
    val mapping = TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.ElasticsearchDialect), variables)
    repository.deleteIndex(indexName)
    repository.createIndex(indexName, mapping)
    log.info(s"Created index [$indexName] at Elasticsearch.")
  }

  def getIndexSettings(indexName: String): String =
    repository.getIndexSettings(indexName)
}
