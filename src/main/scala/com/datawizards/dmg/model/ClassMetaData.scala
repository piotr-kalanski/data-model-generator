package com.datawizards.dmg.model

case class ClassMetaData(
  packageName: String,
  className: String,
  fields: Array[FieldMetaData]
)