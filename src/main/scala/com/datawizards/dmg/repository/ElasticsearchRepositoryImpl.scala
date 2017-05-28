package com.datawizards.dmg.repository

import scalaj.http._

class ElasticsearchRepositoryImpl(url: String) extends ElasticsearchRepository {

  override def updateTemplate(templateName: String, mapping: String): Unit = {
    val endpoint = url + "/_template/" + templateName
    val request = Http(endpoint).put(mapping)
    val response: HttpResponse[String] = request.asString
    if(response.code != 200) {
      throw new Exception(response.body)
    }
  }

  override def getTemplate(templateName: String): String = {
    val endpoint = url + "/_template/" + templateName
    val request = Http(endpoint)
    val response: HttpResponse[String] = request.asString
    response.body
  }

  override def createIndex(indexName: String, mapping: String): Unit = {
    val endpoint = url + "/" + indexName
    val request = Http(endpoint).put(mapping)
    val response: HttpResponse[String] = request.asString
    if(response.code != 200) {
      throw new Exception(response.body)
    }
  }

  override def getIndexSettings(indexName: String): String = {
    val endpoint = url + "/" + indexName
    val request = Http(endpoint)
    val response: HttpResponse[String] = request.asString
    response.body
  }

}
