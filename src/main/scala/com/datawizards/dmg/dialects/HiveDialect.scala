package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata.ClassTypeMetaData
import com.datawizards.dmg.model.{ClassMetaData, FieldMetaData}

import scala.util.Try

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

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s"ARRAY<$elementTypeExpression>"

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s"STRUCT<${fieldNamesWithExpressions.map{case (k,v) => s"$k : $v"}.mkString(", ")}>"

  override def toString: String = "HiveDialect"

  override protected def generateColumnsExpression(classMetaData: ClassMetaData): String =
   if(isAvroSchemaURLProvided(classMetaData)) ""
   else super.generateColumnsExpression(classMetaData)

  override protected def fieldAdditionalExpressions(f: FieldMetaData): String =
    if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'"

  override protected def additionalTableProperties(classMetaData: ClassMetaData): String =
    commentExpression(classMetaData) +
    partitionedByExpression(classMetaData) +
    rowFormatSerdeExpression(classMetaData) +
    storedAsExpression(classMetaData) +
    locationExpression(classMetaData) +
    tablePropertiesExpression(classMetaData)

  override protected def additionalTableExpressions(classMetaData: ClassMetaData): String = ""

  override protected def createTableExpression(classMetaData: ClassMetaData): String =
    s"CREATE ${if(hiveExternalTableLocation(classMetaData).isDefined) "EXTERNAL " else ""}TABLE ${classMetaData.className}"

  override protected def generateColumn(field: FieldMetaData): Boolean =
    !isPartitionField(field)

  private val HivePartitionColumn: String = "com.datawizards.dmg.annotations.hive.hivePartitionColumn"
  private val HiveRowFormatSerde: String = "com.datawizards.dmg.annotations.hive.hiveRowFormatSerde"
  private val HieTableProperty: String = "com.datawizards.dmg.annotations.hive.hiveTableProperty"
  private val HiveStoredAs: String = "com.datawizards.dmg.annotations.hive.hiveStoredAs"
  private val HiveExternalTable: String = "com.datawizards.dmg.annotations.hive.hiveExternalTable"

  private def isPartitionField(field: FieldMetaData): Boolean =
    field
      .annotations
      .exists(_.name == HivePartitionColumn)

  private def isAvroSchemaURLProvided(classMetaData: ClassMetaData): Boolean =
    classMetaData
      .annotations
      .filter(_.name == HieTableProperty)
      .exists(_.attributes.exists(_.value == "avro.schema.url"))

  private def commentExpression(classMetaData: ClassMetaData): String =
    if(classMetaData.comment.isDefined)
      s"""
         |COMMENT '${classMetaData.comment.get}'""".stripMargin
    else ""

  private def partitionedByExpression(classMetaData: ClassMetaData): String =
  {
    val partitionFields = classMetaData.fields.filter(_.annotations.exists(_.name == HivePartitionColumn))
    if(partitionFields.isEmpty)
      ""
    else {
      var fieldOrder = 0
      "\nPARTITIONED BY(" +
        partitionFields
          .map(f => {
            val partitionColumn = f.annotations.find(_.name == HivePartitionColumn).get
            val order = Try(partitionColumn.attributes.find(_.name == "order").get.value.toInt).getOrElse(0)
            fieldOrder += 1
            (f, order, fieldOrder)
          })
          .toSeq
          .sortWith{case (e1,e2) => e1._2 < e2._2 || (e1._2 == e2._2 && e1._3 < e2._3)}
          .map(_._1)
          .map(f => s"${f.name} ${generateTypeExpression(f)}")
          .mkString(", ") +
        ")"
    }
  }

  private def rowFormatSerdeExpression(classMetaData: ClassMetaData): String =
  {
    val rowFormatSerde = classMetaData.getAnnotationValue(HiveRowFormatSerde)
    if(rowFormatSerde.isDefined)
      s"""
         |ROW FORMAT SERDE '${rowFormatSerde.get}'""".stripMargin
    else ""
  }

  private def storedAsExpression(classMetaData: ClassMetaData): String =
  {
    val storedAs = classMetaData.getAnnotationValue(HiveStoredAs)
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
    val tableProperties = classMetaData.annotations.filter(_.name == HieTableProperty)
    if(tableProperties.isEmpty)
      ""
    else
      "\nTBLPROPERTIES(\n   " +
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
    classMetaData.getAnnotationValue(HiveExternalTable)

}
