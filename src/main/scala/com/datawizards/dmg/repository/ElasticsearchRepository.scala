package com.datawizards.dmg.repository

trait ElasticsearchRepository {
  def updateTemplate(templateName: String, mapping: String): Unit
  def getTemplate(templateName: String): String
  def createIndex(indexName: String, mapping: String): Unit
  def getIndexSettings(indexName: String): String
}
