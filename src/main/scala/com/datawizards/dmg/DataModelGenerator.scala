package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import com.datawizards.dmg.metadata._
import com.datawizards.dmg.model.{ClassMetaData, FieldMetaData}
import org.apache.log4j.Logger

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {

  private val log = Logger.getLogger(getClass.getName)

  /**
    * Generate data model for provided class
    *
    * @param dialect DB dialect e.g. H2, Hive, Redshift, Avro schema, Elasticsearch maping
    * @tparam T type for which generate data model
    */
  def generate[T: ClassTag: TypeTag](dialect: Dialect): String = {
    val ct = implicitly[ClassTag[T]].runtimeClass
    log.info(s"Generating model for class: [${ct.getName}], dialect: [$dialect]")
    dialect.generateDataModel(getClassMetaData[T](dialect))
  }

  private def getClassMetaData[T: ClassTag: TypeTag](dialect: Dialect): ClassMetaData = {
    val classMetaData = MetaDataExtractor.extractClassMetaData[T]()

    new ClassMetaData(
      className = getClassName[T](dialect, classMetaData),
      fields = getFieldsMetadata(dialect, classMetaData),
      metaData = classMetaData
    )
  }

  private val Table = "com.datawizards.dmg.annotations.table"
  private val Column = "com.datawizards.dmg.annotations.column"

  private def getClassName[T: ClassTag](dialect: Dialect, classMetaData: ClassTypeMetaData): String = {
    val tableAnnotations = classMetaData.annotations.filter(_.name == Table)
    if(tableAnnotations.nonEmpty) {
      val dialectSpecificTableAnnotation = tableAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificTableAnnotation.isDefined)
        dialectSpecificTableAnnotation.get.attributes.filter(_.name == "name").head.value
      else {
        val defaultTableAnnotation = tableAnnotations.find(!_.attributes.exists(aa => aa.name == "dialect"))
        if(defaultTableAnnotation.isDefined)
          defaultTableAnnotation.get.attributes.filter(_.name == "name").head.value
        else
          implicitly[ClassTag[T]].runtimeClass.getSimpleName
      }
    }
    else
      implicitly[ClassTag[T]].runtimeClass.getSimpleName
  }

  private def getFieldsMetadata(dialect: Dialect, classMetaData: ClassTypeMetaData): Iterable[FieldMetaData] =
    classMetaData
      .fields
      .map(f => new FieldMetaData(
        name = getFieldName(dialect, f),
        fieldMetaData = f
      ))

  private def getFieldName(dialect: Dialect, classFieldMetaData: ClassFieldMetaData): String = {
    val columnAnnotations = classFieldMetaData.annotations.filter(_.name == Column)
    if(columnAnnotations.nonEmpty) {
      val dialectSpecificColumnAnnotation = columnAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificColumnAnnotation.isDefined)
        dialectSpecificColumnAnnotation.get.attributes.filter(_.name == "name").head.value
      else {
        val defaultColumnAnnotation = columnAnnotations.find(!_.attributes.exists(aa => aa.name == "dialect"))
        if(defaultColumnAnnotation.isDefined)
          defaultColumnAnnotation.get.attributes.filter(_.name == "name").head.value
        else
          classFieldMetaData.fieldName
      }
    }
    else
      classFieldMetaData.fieldName
  }

}
