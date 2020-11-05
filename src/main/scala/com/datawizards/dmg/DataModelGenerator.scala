package com.datawizards.dmg

import com.datawizards.dmg.dialects.{Dialect, MetaDataWithDialectExtractor}
import com.datawizards.dmg.generator.Generator
import com.datawizards.dmg.metadata._
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {

  private val log = Logger.getLogger(getClass.getName)

  /**
    * Generate data model for provided class
    *
    * @param generator for a specific dialect, like DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch mapping
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](generator: Generator): String =
    generate(generator, getClassMetaData[T](generator.getSupportedDialect()))

  /**
    * Generate data model for provided class
    *
    * @param generator for a specific dialect, like DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch mapping
    * @param classTypeMetaData extracted class metadata
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](generator: Generator, classTypeMetaData: ClassTypeMetaData): String = {
    val ct = implicitly[ClassTag[T]].runtimeClass
    log.info(s"Generating model for class: [${ct.getName}], dialect: [${generator.getSupportedDialect()}]")
    generateDataModel(generator, classTypeMetaData)
  }

  private def getClassMetaData[T: ClassTag: TypeTag](dialect: Dialect): ClassTypeMetaData =
    MetaDataWithDialectExtractor.extractClassMetaDataForDialect[T](Some(dialect))

  private def generateDataModel(generator: Generator, classTypeMetaData: ClassTypeMetaData): String = {
    generator.generateDataModel(classTypeMetaData, generateFieldsExpressions(generator, classTypeMetaData))
  }

  private def generateFieldsExpressions(generator: Generator, classTypeMetaData: ClassTypeMetaData): Iterable[String] = {
    classTypeMetaData
      .fields
      .withFilter(f => generator.generateColumn(f))
      .map(f => generator.generateClassFieldExpression(f))
  }

}
