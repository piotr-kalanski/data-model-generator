package com.datawizards.dmg.repository

import scalaj.http._
import org.json4s._
import org.json4s.jackson.JsonMethods._

class AvroSchemaRegistryRepositoryImpl(url: String) extends AvroSchemaRegistryRepository {
  override def registerSchema(subject: String, schema: String): Unit = {
    val endpoint = url + "/subjects/" + subject + "/versions"
    val request = Http(endpoint)
      .header("Content-Type", "application/vnd.schemaregistry.v1+json")
      .postData(schema.replace("\r", "").replace("\n", ""))
    val response: HttpResponse[String] = request.asString
    if(response.code != 200) {
      throw new Exception(response.body)
    }
  }

  override def subjects(): Iterable[String] = {
    implicit val formats = DefaultFormats
    val endpoint = url + "/subjects"
    val response = Http(endpoint).asString
    val body = response.body
    val json = parse(body)
    json.extract[Array[String]]
  }

  override def fetchSchema(subject: String, version: String): String = {
    implicit val formats = DefaultFormats
    val endpoint = url + "/subjects/" + subject + "/versions/" + version
    val response = Http(endpoint).asString
    val body = response.body
    val json = parse(body)
    (json \ "schema").extract[String]
  }

}
