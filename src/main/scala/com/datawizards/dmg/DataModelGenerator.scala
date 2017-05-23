package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import com.datawizards.dmg.metadata.{AnnotationMetaData, CaseClassMetaData, CaseClassMetaDataExtractor, ClassFieldMetaData}
import com.datawizards.dmg.model.{ClassMetaData, FieldMetaData}
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types.StructField

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {

  def generate[T: ClassTag: TypeTag](dialect: Dialect): String =
    dialect.generateDataModel(getClassMetaData[T](dialect))

  private def getClassMetaData[T: ClassTag: TypeTag](dialect: Dialect): ClassMetaData = {
    val classMetaData = CaseClassMetaDataExtractor.extractCaseClassMetaData[T]()

    ClassMetaData(
      packageName = getPackageName[T],
      className = getClassName[T](classMetaData),
      comment = getComment(classMetaData.annotations),
      fields = getFieldsMetadata[T](classMetaData, dialect)
    )
  }

  private def getPackageName[T: ClassTag]: String =
    implicitly[ClassTag[T]].runtimeClass.getPackage.getName

  private def getClassName[T: ClassTag](classMetaData: CaseClassMetaData): String = {
    val tableAnnotation = classMetaData.annotations.find(_.name == "com.datawizards.dmg.annotations.table")
    if(tableAnnotation.isDefined)
      tableAnnotation.get.attributes.head.value
    else
      implicitly[ClassTag[T]].runtimeClass.getSimpleName
  }

  private def getFieldsMetadata[T: ClassTag: TypeTag](classMetaData: CaseClassMetaData, dialect: Dialect): Array[FieldMetaData] = {
    val schema = ExpressionEncoder[T].schema
    (schema.fields zip classMetaData.fields)
      .map{case (schemaField, classField) =>
        FieldMetaData(
          getFieldName(schemaField, classField),
          dialect.mapDataType(schemaField.dataType),
          comment = getComment(classField.annotations)
        )
      }
  }

  private def getComment(annotations: Iterable[AnnotationMetaData]): Option[String] = {
    val commentAnnotation = annotations.find(_.name == "com.datawizards.dmg.annotations.comment")
    if(commentAnnotation.isDefined)
      Some(commentAnnotation.get.attributes.head.value)
    else
      None
  }

  private def getFieldName(schemaField: StructField, classFieldMetaData: ClassFieldMetaData): String = {
    val columnAnnotation = classFieldMetaData.annotations.find(_.name == "com.datawizards.dmg.annotations.column")
    if(columnAnnotation.isDefined)
      columnAnnotation.get.attributes.head.value
    else
      schemaField.name
  }

}
