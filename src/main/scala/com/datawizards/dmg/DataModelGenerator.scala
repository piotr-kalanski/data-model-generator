package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import com.datawizards.dmg.metadata.{CaseClassMetaDataExtractor, ClassFieldMetaData}
import com.datawizards.dmg.model.{ClassMetaData, FieldMetaData}
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types.StructField

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {

  def generate[T: ClassTag: TypeTag](dialect: Dialect): String =
    dialect.generateDataModel(getClassMetaData[T](dialect))

  private def getClassMetaData[T: ClassTag: TypeTag](dialect: Dialect): ClassMetaData =
    ClassMetaData(
      packageName = getPackageName[T],
      className = getClassName[T],
      fields = getFieldsMetadata[T](dialect)
    )

  private def getPackageName[T: ClassTag]: String =
    implicitly[ClassTag[T]].runtimeClass.getPackage.getName

  private def getClassName[T: ClassTag]: String =
    implicitly[ClassTag[T]].runtimeClass.getSimpleName

  private def getFieldsMetadata[T: ClassTag: TypeTag](dialect: Dialect): Array[FieldMetaData] = {
    val schema = ExpressionEncoder[T].schema
    val classMetaData = CaseClassMetaDataExtractor.extractCaseClassMetaData[T]()
    (schema.fields zip classMetaData.fields)
      .map{case (schemaField, classField) =>
        FieldMetaData(
          getFieldName(schemaField, classField),
          dialect.mapDataType(schemaField.dataType)
        )
      }
  }

  private def getFieldName(schemaField: StructField, classFieldMetaData: ClassFieldMetaData): String = {
    val columnAnnotation = classFieldMetaData.annotations.find(_.name == "com.datawizards.dmg.annotations.column")
    if(columnAnnotation.isDefined)
      columnAnnotation.get.attributes.head.value
    else
      schemaField.name
  }

}
