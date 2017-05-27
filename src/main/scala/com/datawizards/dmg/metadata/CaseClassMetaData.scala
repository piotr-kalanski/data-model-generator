package com.datawizards.dmg.metadata

@deprecated
case class CaseClassMetaData(
                              packageName: String,
                              className: String,
                              fields: Seq[CaseClassFieldMetaData],
                              annotations: Seq[AnnotationMetaData]
)
