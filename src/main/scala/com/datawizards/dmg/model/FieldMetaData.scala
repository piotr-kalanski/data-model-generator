package com.datawizards.dmg.model

case class FieldMetaData(
  name: String,
  targetType: FieldType,
  length: Option[String],
  comment: Option[String]
)