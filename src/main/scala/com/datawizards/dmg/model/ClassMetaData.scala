package com.datawizards.dmg.model

case class ClassMetaData(
  packageName: String,
  className: String,
  comment: Option[String],
  fields: Array[FieldMetaData]
)