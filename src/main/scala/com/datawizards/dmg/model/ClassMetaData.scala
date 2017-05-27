package com.datawizards.dmg.model

import com.datawizards.dmg.metadata.{AnnotationMetaData, ClassTypeMetaData}

class ClassMetaData(
  val className: String,
  val fields: Iterable[FieldMetaData],
  metaData: ClassTypeMetaData
) extends BaseMetaData {

  def packageName: String = metaData.packageName

  override def annotations: Iterable[AnnotationMetaData] = metaData.annotations

}