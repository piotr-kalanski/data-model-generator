package com.datawizards.dmg.model
@deprecated
sealed trait FieldType {
  val name: String
}
@deprecated
case class PrimitiveFieldType(name: String) extends FieldType
@deprecated
case class ArrayFieldType(name: String, elementType: FieldType) extends FieldType
@deprecated
case class StructFieldType(name: String, typeName: String, fields: Map[String,FieldType]) extends FieldType
