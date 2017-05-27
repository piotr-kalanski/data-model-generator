package com.datawizards.dmg.metadata

@deprecated
case class CaseClassFieldMetaData(
  name: String,
  typeName: String,
  annotations: Seq[AnnotationMetaData]
)
