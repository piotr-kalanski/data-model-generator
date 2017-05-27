package com.datawizards.dmg.model

import com.datawizards.dmg.metadata.AnnotationMetaData
@deprecated
case class FieldMetaData(
  name: String,
  targetType: FieldType,
  length: Option[String],
  comment: Option[String],
  annotations: Iterable[AnnotationMetaData]
)