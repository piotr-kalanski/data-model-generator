package com.datawizards.dmg.model

case class FieldMetaData(
  name: String,
  targetType: String,
  length: Option[String],
  comment: Option[String]
)