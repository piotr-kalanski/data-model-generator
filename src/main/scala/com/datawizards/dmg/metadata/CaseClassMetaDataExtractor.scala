package com.datawizards.dmg.metadata

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Class responsible for extracting using reflections metadata about case class: fields, annotations with attributes.
  * It is used to simplify getting such information, because current Scala API is very hard to use.
  */
object CaseClassMetaDataExtractor {
  def extractCaseClassMetaData[T : ClassTag : TypeTag](): CaseClassMetaData = {
    val ct = implicitly[ClassTag[T]]
    val klass = ct.runtimeClass

    CaseClassMetaData(
      packageName = klass.getPackage.getName,
      className = klass.getSimpleName,
      annotations = extractAnnotations(symbolOf[T].asClass),
      fields = symbolOf[T]
        .asClass
        .primaryConstructor
        .typeSignature
        .paramLists
        .head
        .map(f => ClassFieldMetaData(
          name = f.name.toString,
          annotations = extractAnnotations(f)
        ))
    )
  }

  def getAnnotationValue(annotations: Iterable[AnnotationMetaData], annotationName: String): Option[String] = {
    val annotation = annotations.find(_.name == annotationName)
    if(annotation.isDefined)
      Some(annotation.get.attributes.head.value)
    else
      None
  }

  private def extractAnnotations(symbol: Symbol): Seq[AnnotationMetaData] =
    symbol
      .annotations
      .map(a =>
        AnnotationMetaData(
          name = a.tree.tpe.toString,
          attributes = {
            val params = a.tree.tpe.decls.find(_.name.toString == "<init>").get.asMethod.paramLists.flatten.map(f => f.name.toString)
            val values = a.tree.children.tail
            (params zip values)
              .map(pv => AnnotationAttributeMetaData(
                name = pv._1,
                value = pv._2.toString.stripPrefix("\"").stripSuffix("\"")
              ))
          }
        )
      )
}
