package com.datawizards.dmg.model

sealed trait FieldType {
  val name: String
}
case class PrimitiveFieldType(name: String) extends FieldType
case class ArrayFieldType(name: String, elementType: FieldType) extends FieldType
case class StructFieldType(name: String, fields: Map[String,FieldType]) extends FieldType
