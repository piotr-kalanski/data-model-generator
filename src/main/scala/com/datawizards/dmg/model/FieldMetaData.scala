package com.datawizards.dmg.model

case class FieldMetaData(
  name: String,
  targetType: String,
  comment: Option[String]
)