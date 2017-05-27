package com.datawizards.dmg.model

import com.datawizards.dmg.metadata.AnnotationMetaData

trait BaseMetaData {
  private val Comment = "com.datawizards.dmg.annotations.comment"

  def annotations: Iterable[AnnotationMetaData]

  def comment: Option[String] = getAnnotationValue(Comment)

  def getAnnotationValue(annotationName: String): Option[String] = {
    val annotation = annotations.find(_.name == annotationName)
    if(annotation.isDefined)
      Some(annotation.get.attributes.head.value)
    else
      None
  }
}
