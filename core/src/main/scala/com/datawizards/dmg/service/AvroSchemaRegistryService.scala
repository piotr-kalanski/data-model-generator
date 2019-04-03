package com.datawizards.dmg.service

import com.datawizards.dmg.{DataModelGenerator, dialects}
import com.datawizards.dmg.repository.AvroSchemaRegistryRepository

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import org.apache.log4j.Logger

trait AvroSchemaRegistryService {
  private val log = Logger.getLogger(getClass.getName)
  protected val repository: AvroSchemaRegistryRepository
  protected val hdfsService: HDFSService

  def generateSchema[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): String = {
    TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.AvroSchemaDialect), variables)
  }

  def generateSchemaForAvroSchemaRegistry[T: ClassTag: TypeTag](variables: Map[String, String] = Map.empty): String = {
    TemplateHandler.inflate(DataModelGenerator.generate[T](dialects.AvroSchemaRegistryDialect), variables)
  }

  def registerSchema[T: ClassTag: TypeTag](subject: String): Unit = {
    val schema = generateSchemaForAvroSchemaRegistry[T]()
    registerSchema(schema, subject)
  }

  def registerSchema(schema: String, subject: String): Unit = {
    repository.registerSchema(subject, schema)
    log.info(s"Registered schema [$subject] at avro schema registry.")
  }

  def subjects(): Iterable[String] =
    repository.subjects()

  def fetchSchema(subject: String, version: String): String =
    repository.fetchSchema(subject, version)

  def fetchSchema(subject: String): String =
    fetchSchema(subject, "latest")

  def copyAvroSchemaToHdfs(schema: String, hdfsPath: String): Unit = {
    val file = "/tmp/dmg_" + java.util.UUID.randomUUID().toString
    hdfsService.copyLocalFileToHDFS(file, hdfsPath)
  }

  def copyAvroSchemaToHdfs[T: ClassTag: TypeTag](hdfsPath: String): Unit = {
    val schema = generateSchema[T]()
    copyAvroSchemaToHdfs(schema, hdfsPath)
  }
}
