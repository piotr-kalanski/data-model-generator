package com.datawizards.dmg.metadata

case class CaseClassMetaData(
  packageName: String,
  className: String,
  fields: Seq[ClassFieldMetaData],
  annotations: Seq[AnnotationMetaData]
)
