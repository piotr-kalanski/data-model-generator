package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.CaseClassMetaDataExtractor
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

  override def toString: String = "HiveDialect"

  override protected def generateColumnsExpression(classMetaData: ClassMetaData): String =
   if(isAvroSchemaURLProvided(classMetaData)) ""
   else super.generateColumnsExpression(classMetaData)

  override protected def fieldAdditionalExpressions(f: FieldMetaData): String =
    if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'"

  override protected def additionalTableProperties(classMetaData: ClassMetaData): String =
    commentExpression(classMetaData) +
    rowFormatSerdeExpression(classMetaData) +
    storedAsExpression(classMetaData) +
    locationExpression(classMetaData) +
    tablePropertiesExpression(classMetaData)

  override protected def additionalTableExpressions(classMetaData: ClassMetaData): String = ""

  override protected def getArrayType(a: ArrayFieldType): String =
    s"${a.name}<${getFieldType(a.elementType)}>"

  override protected def getStructType(s: StructFieldType): String =
    s"${s.name}<${s.fields.map{case (k,v) => s"$k : ${v.name}"}.mkString(", ")}>"

  override protected def createTableExpression(classMetaData: ClassMetaData): String =
    s"CREATE ${if(hiveExternalTableLocation(classMetaData).isDefined) "EXTERNAL " else ""}TABLE ${classMetaData.className}"

  private def isAvroSchemaURLProvided(classMetaData: ClassMetaData): Boolean =
    classMetaData
      .annotations
      .filter(_.name == "com.datawizards.dmg.annotations.hive.hiveTableProperty")
      .exists(_.attributes.exists(_.value == "avro.schema.url"))

  private def commentExpression(classMetaData: ClassMetaData): String =
    if(classMetaData.comment.isDefined)
      s"""
         |COMMENT '${classMetaData.comment.get}'""".stripMargin
    else ""

  private def rowFormatSerdeExpression(classMetaData: ClassMetaData): String =
  {
    val rowFormatSerde = CaseClassMetaDataExtractor.getAnnotationValue(classMetaData.annotations, "com.datawizards.dmg.annotations.hive.hiveRowFormatSerde")
    if(rowFormatSerde.isDefined)
      s"""
         |ROW FORMAT SERDE '${rowFormatSerde.get}'""".stripMargin
    else ""
  }

  private def storedAsExpression(classMetaData: ClassMetaData): String =
  {
    val storedAs = CaseClassMetaDataExtractor.getAnnotationValue(classMetaData.annotations, "com.datawizards.dmg.annotations.hive.hiveStoredAs")
    if(storedAs.isDefined)
      s"""
         |STORED AS ${storedAs.get.replace("\\'","'")}""".stripMargin
    else ""
  }

  private def locationExpression(classMetaData: ClassMetaData): String =
  {
    val externalTableLocation = hiveExternalTableLocation(classMetaData)
    if(externalTableLocation.isDefined)
      s"""
         |LOCATION '${externalTableLocation.get}'""".stripMargin
    else ""
  }

  private def tablePropertiesExpression(classMetaData: ClassMetaData): String =
  {
    val tableProperties = classMetaData.annotations.filter(_.name == "com.datawizards.dmg.annotations.hive.hiveTableProperty")
    if(tableProperties.isEmpty)
      ""
    else
      "TBLPROPERTIES(\n   " +
      tableProperties
        .map(a => {
          val key = a.attributes.find(_.name == "key").get.value
          val value = a.attributes.find(_.name == "value").get.value
          s"'$key' = '$value'"
        })
      .mkString(",\n   ") +
      "\n)"
  }

  private def hiveExternalTableLocation(classMetaData: ClassMetaData): Option[String] =
    CaseClassMetaDataExtractor.getAnnotationValue(classMetaData.annotations, "com.datawizards.dmg.annotations.hive.hiveExternalTable")

}
