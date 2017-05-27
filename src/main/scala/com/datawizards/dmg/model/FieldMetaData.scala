package com.datawizards.dmg.model

import com.datawizards.dmg.metadata.{AnnotationMetaData, ClassFieldMetaData, TypeMetaData}

class FieldMetaData(
  val name: String,
  fieldMetaData: ClassFieldMetaData
) extends BaseMetaData {

  private val Length = "com.datawizards.dmg.annotations.length"

  def length: Option[String] = getAnnotationValue(Length)

  def fieldType: TypeMetaData = fieldMetaData.fieldType

  override def annotations: Iterable[AnnotationMetaData] = fieldMetaData.annotations
}