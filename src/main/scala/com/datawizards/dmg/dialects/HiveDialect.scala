package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldMetaData}

object HiveDialect extends DatabaseDialect {
  override def intType: String = "INT"

  override def stringType: String = "STRING"

  override def longType: String = "BIGINT"

  override def doubleType: String = "DOUBLE"

  override def floatType: String = "FLOAT"

  override def shortType: String = "SMALLINT"

  override def booleanType: String = "BOOLEAN"

  override def byteType: String = "TINYINT"

  override def dateType: String = "DATE"

  override def timestampType: String = "TIMESTAMP"

  override def arrayType: String = "ARRAY"

  override protected def fieldAdditionalExpressions(f: FieldMetaData): String =
    if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'"

  override protected def additionalTableProperties(classMetaData: ClassMetaData): String =
    if(classMetaData.comment.isDefined)
      s"""
         |COMMENT '${classMetaData.comment.get}'""".stripMargin
    else ""

  override protected def additionalTableExpressions(classMetaData: ClassMetaData): String = ""

  override protected def getArrayType(a: ArrayFieldType): String =
    s"${a.name}<${getFieldType(a.elementType)}>"

  override def toString: String = "HiveDialect"
}
