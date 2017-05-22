package com.datawizards.dmg.metadata

case class AnnotationMetaData (
  name: String,
  attributes: Seq[AnnotationAttributeMetaData]
)
