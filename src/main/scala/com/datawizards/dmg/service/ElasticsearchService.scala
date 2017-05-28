package com.datawizards.dmg.service

import com.datawizards.dmg.repository.ElasticsearchRepository
import com.datawizards.dmg.{DataModelGenerator, dialects}
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

trait ElasticsearchService {
  private val log = Logger.getLogger(getClass.getName)
  protected val repository: ElasticsearchRepository

  def updateTemplate[T: ClassTag: TypeTag](templateName: String): Unit = {
    val template = DataModelGenerator.generate[T](dialects.Elasticsearch)
    repository.updateTemplate(templateName, template)
    log.info(s"Updated template [$templateName] at Elasticsearch.")
  }

  def getTemplate(templateName: String): String =
    repository.getTemplate(templateName)

  def createIndex[T: ClassTag: TypeTag](indexName: String): Unit = {
    val mapping = DataModelGenerator.generate[T](dialects.Elasticsearch)
    repository.createIndex(indexName, mapping)
    log.info(s"Created index [$indexName] at Elasticsearch.")
  }

  def getIndexSettings(indexName: String): String =
    repository.getIndexSettings(indexName)
}
