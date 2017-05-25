package com.datawizards.dmg.dialects
import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldMetaData, StructFieldType}

object H2Dialect extends DatabaseDialect {
  override def intType: String = "INT"

  override def stringType: String = "VARCHAR"

  override def longType: String = "BIGINT"

  override def doubleType: String = "DOUBLE"

  override def floatType: String = "REAL"

  override def shortType: String = "SMALLINT"

  override def booleanType: String = "BOOLEAN"

  override def byteType: String = "TINYINT"

  override def dateType: String = "DATE"

  override def timestampType: String = "TIMESTAMP"

  override def arrayType: String = "ARRAY"

  override def structType: String = "OTHER"

  override protected def fieldAdditionalExpressions(f: FieldMetaData): String =
    if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'"

  override protected def additionalTableProperties(classMetaData: ClassMetaData): String = ""

  override protected def additionalTableExpressions(classMetaData: ClassMetaData): String =
    if(classMetaData.comment.isDefined)
      s"""
         |COMMENT ON TABLE ${classMetaData.className} IS '${classMetaData.comment.get}';""".stripMargin
    else ""

  override protected def getArrayType(a: ArrayFieldType): String = a.name

  override protected def getStructType(s: StructFieldType): String = s.name

  override def toString: String = "H2Dialect"
}
