package com.datawizards.dmg.metadata

case class ClassFieldMetaData(
  name: String,
  typeName: String,
  annotations: Seq[AnnotationMetaData]
)
