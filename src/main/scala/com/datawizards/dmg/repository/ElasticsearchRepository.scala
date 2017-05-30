package com.datawizards.dmg.repository

trait ElasticsearchRepository {
  def updateTemplate(templateName: String, mapping: String): Unit
  def getTemplate(templateName: String): String
  def deleteTemplate(templateName: String): Unit
  def templateExists(templateName: String): Boolean
  def createIndex(indexName: String, mapping: String): Unit
  def getIndexSettings(indexName: String): String
  def deleteIndex(indexName: String): Unit
  def indexExists(indexName: String): Boolean
}
