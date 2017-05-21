package com.datawizards.dmg.service

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.dmg.repository.AvroSchemaRegistryRepository

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import org.apache.log4j.Logger

trait AvroSchemaRegistryService {
  private val log = Logger.getLogger(getClass.getName)
  protected val repository: AvroSchemaRegistryRepository

  def registerSchema[T: ClassTag: TypeTag](subject: String): Unit = {
    val schema = DataModelGenerator.generate[T](dialects.AvroSchemaRegistry)
    repository.registerSchema(subject, schema)
    log.info(s"Registered schema [$subject] at avro schema registry.")
  }

  def subjects(): Iterable[String] =
    repository.subjects()

  def fetchSchema(subject: String, version: String): String =
    repository.fetchSchema(subject, version)

  def fetchSchema(subject: String): String =
    fetchSchema(subject, "latest")

}
