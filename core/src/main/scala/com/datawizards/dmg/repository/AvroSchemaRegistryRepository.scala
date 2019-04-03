package com.datawizards.dmg.repository

trait AvroSchemaRegistryRepository {
  def registerSchema(subject: String, schema: String): Unit
  def subjects(): Iterable[String]
  def fetchSchema(subject: String, version: String): String
}
