package com.datawizards.dmg.model

import com.datawizards.dmg.metadata.AnnotationMetaData
@deprecated
case class ClassMetaData(
  packageName: String,
  className: String,
  comment: Option[String],
  fields: Array[FieldMetaData],
  annotations: Iterable[AnnotationMetaData]
)