package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import com.datawizards.dmg.metadata._
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {

  private val log = Logger.getLogger(getClass.getName)

  /**
    * Generate data model for provided class
    *
    * @param dialect DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch mapping
    * @param variables key-value map used to replace placeholder variables
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](dialect: Dialect, variables: Map[String, String] = Map.empty): String =
    generate(dialect, getClassMetaData[T](dialect), variables)

  /**
    * Generate data model for provided class
    *
    * @param dialect DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch mapping
    * @param classTypeMetaData extracted class metadata
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](dialect: Dialect, classTypeMetaData: ClassTypeMetaData): String =
    generate(dialect, classTypeMetaData, Map.empty)

  /**
    * Generate data model for provided class
    *
    * @param dialect DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch mapping
    * @param classTypeMetaData extracted class metadata
    * @param variables key-value map used to replace placeholder variables
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](dialect: Dialect, classTypeMetaData: ClassTypeMetaData, variables: Map[String, String]): String = {
    val ct = implicitly[ClassTag[T]].runtimeClass
    log.info(s"Generating model for class: [${ct.getName}], dialect: [$dialect]")
    replacePlaceholderVariables(generateDataModel(dialect, classTypeMetaData), variables)
  }

  private def getClassMetaData[T: ClassTag: TypeTag](dialect: Dialect): ClassTypeMetaData =
    MetaDataExtractor.extractClassMetaDataForDialect[T](dialect)

  private def generateDataModel(dialect: Dialect, classTypeMetaData: ClassTypeMetaData): String = {
    dialect.generateDataModel(classTypeMetaData, generateFieldsExpressions(dialect, classTypeMetaData))
  }

  private def generateFieldsExpressions(dialect: Dialect, classTypeMetaData: ClassTypeMetaData): Iterable[String] = {
    classTypeMetaData
      .fields
      .withFilter(f => dialect.generateColumn(f))
      .map(f => dialect.generateClassFieldExpression(f))
  }


  private def replacePlaceholderVariables(template: String, variables: Map[String, String]): String = {
    val re = "\\$\\{.*?\\}".r
    var result = template
    for(k <- re.findAllIn(template))
      result = result.replace(k, variables(k.substring(2, k.length-1)))
    result
  }


}
