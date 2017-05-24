package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import com.datawizards.dmg.metadata.{AnnotationMetaData, CaseClassMetaData, CaseClassMetaDataExtractor, ClassFieldMetaData}
import com.datawizards.dmg.model.{ClassMetaData, FieldMetaData}
import org.apache.log4j.Logger
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types.StructField

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
    val classMetaData = CaseClassMetaDataExtractor.extractCaseClassMetaData[T]()

    ClassMetaData(
      packageName = getPackageName[T],
      className = getClassName[T](dialect, classMetaData),
      comment = getComment(classMetaData.annotations),
      fields = getFieldsMetadata[T](classMetaData, dialect)
    )
  }

  private def getPackageName[T: ClassTag]: String =
    implicitly[ClassTag[T]].runtimeClass.getPackage.getName

  private def getClassName[T: ClassTag](dialect: Dialect, classMetaData: CaseClassMetaData): String = {
    val tableAnnotations = classMetaData.annotations.filter(_.name == "com.datawizards.dmg.annotations.table")
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

  private def getFieldsMetadata[T: ClassTag: TypeTag](classMetaData: CaseClassMetaData, dialect: Dialect): Array[FieldMetaData] = {
    val schema = ExpressionEncoder[T].schema
    (schema.fields zip classMetaData.fields)
      .map{case (schemaField, classField) =>
        FieldMetaData(
          getFieldName(dialect, schemaField, classField),
          dialect.mapDataType(schemaField.dataType),
          length = getAnnotationValue(classField.annotations, "com.datawizards.dmg.annotations.length"),
          comment = getComment(classField.annotations)
        )
      }
  }

  private def getComment(annotations: Iterable[AnnotationMetaData]): Option[String] =
    getAnnotationValue(annotations, "com.datawizards.dmg.annotations.comment")

  private def getAnnotationValue(annotations: Iterable[AnnotationMetaData], annotationName: String): Option[String] = {
    val annotation = annotations.find(_.name == annotationName)
    if(annotation.isDefined)
      Some(annotation.get.attributes.head.value)
    else
      None
  }

  private def getFieldName(dialect: Dialect, schemaField: StructField, classFieldMetaData: ClassFieldMetaData): String = {
    val columnAnnotations = classFieldMetaData.annotations.filter(_.name == "com.datawizards.dmg.annotations.column")
    if(columnAnnotations.nonEmpty) {
      val dialectSpecificColumnAnnotation = columnAnnotations.find(_.attributes.exists(aa => aa.name == "dialect" && aa.value.contains(dialect.toString.replace("Dialect",""))))
      if(dialectSpecificColumnAnnotation.isDefined)
        dialectSpecificColumnAnnotation.get.attributes.filter(_.name == "name").head.value
      else {
        val defaultColumnAnnotation = columnAnnotations.find(!_.attributes.exists(aa => aa.name == "dialect"))
        if(defaultColumnAnnotation.isDefined)
          defaultColumnAnnotation.get.attributes.filter(_.name == "name").head.value
        else
          schemaField.name
      }
    }
    else
      schemaField.name
  }

}
