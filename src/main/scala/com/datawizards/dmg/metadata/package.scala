package com.datawizards.dmg

package object metadata {
  case class AnnotationAttributeMetaData(name: String, value: String)

  case class AnnotationMetaData(name: String, attributes: Seq[AnnotationAttributeMetaData])

  trait HasAnnotations {
    val annotations: Iterable[AnnotationMetaData]

    def getAnnotationValue(annotationName: String): Option[String] = {
      val annotation = annotations.find(_.name == annotationName)
      if(annotation.isDefined)
        Some(annotation.get.attributes.head.value)
      else
        None
    }

    def annotationExists(annotationName: String): Boolean = {
      val annotation = annotations.find(_.name == annotationName)
      annotation.isDefined
    }

    def getAnnotationsByName(annotationName: String): Iterable[AnnotationMetaData] =
      annotations.filter(_.name == annotationName)
  }

  sealed trait TypeMetaData {
    val packageName: String
    val typeName: String
  }

  sealed abstract class PrimitiveTypeMetaData(val packageName: String, val typeName: String) extends TypeMetaData

  case object IntegerType extends PrimitiveTypeMetaData("scala", "Int")
  case object StringType extends PrimitiveTypeMetaData("scala", "String")
  case object LongType extends PrimitiveTypeMetaData("scala", "Long")
  case object DoubleType extends PrimitiveTypeMetaData("scala", "Double")
  case object FloatType extends PrimitiveTypeMetaData("scala", "Float")
  case object ShortType extends PrimitiveTypeMetaData("scala", "Short")
  case object ByteType extends PrimitiveTypeMetaData("scala", "Byte")
  case object BooleanType extends PrimitiveTypeMetaData("scala", "Boolean")
  case object BinaryType extends PrimitiveTypeMetaData("scala", "Binary")
  case object TimestampType extends PrimitiveTypeMetaData("java.sql", "Timestamp")
  case object DateType extends PrimitiveTypeMetaData("java.util", "Date")
  case object BigDecimalType extends PrimitiveTypeMetaData("scala", "BigDecimal")
  case object BigIntegerType extends PrimitiveTypeMetaData("scala.math", "BigInt")

  case class CollectionTypeMetaData(
    elementType: TypeMetaData
  ) extends TypeMetaData {
    val packageName: String = "scala"
    val typeName: String = "Seq"
  }

  case class MapTypeMetaData(
    keyType: TypeMetaData,
    valueType: TypeMetaData
  ) extends TypeMetaData {
    val packageName: String = "scala"
    val typeName: String = "Map"
  }

  case class ClassFieldMetaData(
    fieldName: String,
    fieldType: TypeMetaData,
    annotations: Iterable[AnnotationMetaData]
  ) extends HasAnnotations

  case class ClassTypeMetaData(
    packageName: String,
    typeName: String,
    annotations: Iterable[AnnotationMetaData],
    fields: Iterable[ClassFieldMetaData]
  ) extends TypeMetaData with HasAnnotations

}
