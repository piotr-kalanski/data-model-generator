package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.{AnnotationMetaData, CaseClassMetaDataExtractor}
import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldMetaData, StructFieldType}

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

  override def structType: String = "STRUCT"

  override protected def fieldAdditionalExpressions(f: FieldMetaData): String =
    if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'"

  override protected def additionalTableProperties(classMetaData: ClassMetaData): String =
  (
    if(classMetaData.comment.isDefined)
      s"""
         |COMMENT '${classMetaData.comment.get}'""".stripMargin
    else ""
  ) +
  {
    val externalTableLocation = hiveExternalTableLocation(classMetaData)
    if(externalTableLocation.isDefined)
      s"""
         |LOCATION '${externalTableLocation.get}'""".stripMargin
    else ""
  }

  override protected def additionalTableExpressions(classMetaData: ClassMetaData): String = ""

  override protected def getArrayType(a: ArrayFieldType): String =
    s"${a.name}<${getFieldType(a.elementType)}>"

  override protected def getStructType(s: StructFieldType): String =
    s"${s.name}<${s.fields.map{case (k,v) => s"$k : ${v.name}"}.mkString(", ")}>"

  override protected def createTableExpression(classMetaData: ClassMetaData): String =
    s"CREATE ${if(hiveExternalTableLocation(classMetaData).isDefined) "EXTERNAL " else ""}TABLE ${classMetaData.className}"

  override def toString: String = "HiveDialect"

  private def hiveExternalTableLocation(classMetaData: ClassMetaData): Option[String] =
    CaseClassMetaDataExtractor.getAnnotationValue(classMetaData.annotations, "com.datawizards.dmg.annotations.hive.hiveExternalTable")

}
