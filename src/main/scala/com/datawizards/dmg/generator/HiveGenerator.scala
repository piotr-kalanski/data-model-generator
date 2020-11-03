package com.datawizards.dmg.generator

import com.datawizards.dmg.dialects.{Dialect, HiveDialect}
import com.datawizards.dmg.generator.DropAndCreateTable.DropAndCreateTable
import com.datawizards.dmg.metadata._

import scala.util.Try

class HiveGenerator(dropAndCreateTable: DropAndCreateTable=DropAndCreateTable.OnlyForExternalTables) extends DatabaseGenerator {


  def getSupportedDialect(): Dialect = HiveDialect

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

  override def binaryType: String = "BINARY"

  override def bigDecimalType: String = "DECIMAL(38,18)"

  override def bigIntegerType: String = "BIGINT"

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    s"ARRAY<$elementTypeExpression>"

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    s"STRUCT<${classTypeMetaData.fields.map(f => s"${f.fieldName} : ${generateTypeExpression(f.fieldType)}").mkString(", ")}>"

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String =
    s"MAP<$keyExpression, $valueExpression>"



  override protected def generateColumnsExpression(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    if(isAvroSchemaURLProvided(classTypeMetaData)) ""
    else super.generateColumnsExpression(classTypeMetaData, fieldsExpressions)

  override protected def fieldAdditionalExpressions(f: ClassFieldMetaData): String =
    if(comment(f).isEmpty) "" else s" COMMENT '${comment(f).get}'"

  override protected def additionalTableProperties(classTypeMetaData: ClassTypeMetaData): String =
    commentExpression(classTypeMetaData) +
      partitionedByExpression(classTypeMetaData) +
      rowFormatSerdeExpression(classTypeMetaData) +
      storedAsExpression(classTypeMetaData) +
      locationExpression(classTypeMetaData) +
      tablePropertiesExpression(classTypeMetaData)

  override protected def additionalTableExpressions(classTypeMetaData: ClassTypeMetaData): String = {
    if(hiveExternalTableLocation(classTypeMetaData).isDefined && partitionedByExpression(classTypeMetaData).nonEmpty)
      s"MSCK REPAIR TABLE ${classTypeMetaData.typeName};\n"
    else
      ""
  }

  override protected def createTableExpression(classTypeMetaData: ClassTypeMetaData): String =
      (
        if(dropAndCreateTable == DropAndCreateTable.Always || (dropAndCreateTable == DropAndCreateTable.OnlyForExternalTables && hiveExternalTableLocation(classTypeMetaData).isDefined)){
          s"DROP TABLE IF EXISTS ${classTypeMetaData.typeName};\n"
        } else ""
      ) +
      (
        if(hiveExternalTableLocation(classTypeMetaData).isDefined){
          s"CREATE EXTERNAL TABLE ${classTypeMetaData.typeName}"
        }else{
          s"CREATE TABLE ${classTypeMetaData.typeName}"
        }
      )
//    s"CREATE ${if(hiveExternalTableLocation(classTypeMetaData).isDefined) "EXTERNAL " else ""}TABLE ${classTypeMetaData.typeName}"

  override def generateColumn(f: ClassFieldMetaData): Boolean =
    !isPartitionField(f)

  /**
    * This property is added to table DDL, in TBLPROPERTIES statement.
    * It contains hash of metadata of a case class from which DDL was generated.
    * This serves as a mechanism for checking if case class (or table definition) has been modified.
    * Different hashes mean that case class has been changed, so DROP TABLE and CREATE TABLE must be run.
    * Same hashes mean that table will not be dropped and created. This saves expensive operations on Glue dropping and creating partitions.
    */
  val tableDefinitionHashPropertyName = "MODEL_GENERATOR_METADATA_HASH"

  def shouldTableBeReCreated(classTypeMetaData: ClassTypeMetaData):Boolean = {
    !getHashFromTableDefinition(classTypeMetaData).contains(getClassTypeMetaDataHash(classTypeMetaData))
  }

  /**
    * Override this method with one that connects to Hive, fetches metadata for a table
    * and returns value of property referenced in `tableDefinitionHashPropertyName`.
    * That property holds hash of metadata to be compared before table creation.
    * Default implementation of this method returns None, meaning that table will be always re-created.
    */
  def getHashFromTableDefinition(metadata: ClassTypeMetaData): Option[Long] = {
    None
  }

  def getClassTypeMetaDataHash(classTypeMetaData: ClassTypeMetaData):Long = {
    log.debug("classTypeMetaData = " + classTypeMetaData)
    log.debug("classTypeMetaData.hashCode() = " + classTypeMetaData.hashCode())
    classTypeMetaData.hashCode()
  }

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String = {
    if(shouldTableBeReCreated(classTypeMetaData)){
      super.generateDataModel(classTypeMetaData, fieldsExpressions)
    } else{
      s"/* Not re-creating table for class ${classTypeMetaData.originalTypeName} because it was not modified. \n" +
        super.generateDataModel(classTypeMetaData, fieldsExpressions) +
        "\n*/\n"
    }
  }

  private val HivePartitionColumn: String = "com.datawizards.dmg.annotations.hive.hivePartitionColumn"
  private val HiveRowFormatSerde: String = "com.datawizards.dmg.annotations.hive.hiveRowFormatSerde"
  private val HiveTableProperty: String = "com.datawizards.dmg.annotations.hive.hiveTableProperty"
  private val HiveStoredAs: String = "com.datawizards.dmg.annotations.hive.hiveStoredAs"
  private val HiveExternalTable: String = "com.datawizards.dmg.annotations.hive.hiveExternalTable"

  private def isPartitionField(field: ClassFieldMetaData): Boolean =
    field
      .annotations
      .exists(_.name == HivePartitionColumn)

  private def isAvroSchemaURLProvided(classTypeMetaData: ClassTypeMetaData): Boolean =
    classTypeMetaData
      .annotations
      .filter(_.name == HiveTableProperty)
      .exists(_.attributes.exists(_.value == "avro.schema.url"))

  private def commentExpression(classTypeMetaData: ClassTypeMetaData): String =
    if(comment(classTypeMetaData).isDefined)
      s"""
         |COMMENT '${comment(classTypeMetaData).get}'""".stripMargin
    else ""

  private def partitionedByExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val partitionFields = getPartitionFields(classTypeMetaData)
    if(partitionFields.isEmpty)
      ""
    else {
      var fieldOrder = 0
      "\nPARTITIONED BY(" +
        partitionFields
          .map(f => s"${f.fieldName} ${generateTypeExpression(f.fieldType)}")
          .mkString(", ") +
        ")"
    }
  }

  private def getPartitionFields(classTypeMetaData: ClassTypeMetaData): Seq[ClassFieldMetaData] = {
    val partitionFields = classTypeMetaData.fields.filter(_.annotations.exists(_.name == HivePartitionColumn))
    var fieldOrder = 0
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
  }

  private def rowFormatSerdeExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val rowFormatSerde = classTypeMetaData.getAnnotationValue(HiveRowFormatSerde)
    if(rowFormatSerde.isDefined)
      s"""
         |ROW FORMAT SERDE '${rowFormatSerde.get}'""".stripMargin
    else ""
  }

  private def storedAsExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val storedAs = classTypeMetaData.getAnnotationValue(HiveStoredAs)
    if(storedAs.isDefined)
      s"""
         |STORED AS ${storedAs.get.replace("\\'","'")}""".stripMargin
    else ""
  }

  private def locationExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val externalTableLocation = hiveExternalTableLocation(classTypeMetaData)
    if(externalTableLocation.isDefined)
      s"""
         |LOCATION '${externalTableLocation.get}'""".stripMargin
    else ""
  }

  private def tablePropertiesExpression(classTypeMetaData: ClassTypeMetaData): String =
  {
    val tableProperties = classTypeMetaData.annotations.filter(_.name == HiveTableProperty)
    val kvItems: Iterable[(String, String)] = tableProperties.map(a => {
      val key = a.attributes.find(_.name == "key").get.value
      val value = a.attributes.find(_.name == "value").get.value
      (key, value)
    }).toList ++ List((tableDefinitionHashPropertyName, s"${getClassTypeMetaDataHash(classTypeMetaData)}"))

    "\nTBLPROPERTIES(\n   " +
      kvItems.map(a => s"'${a._1}' = '${a._2}'")
        .mkString(",\n   ") +
      "\n)"
  }

  private def hiveExternalTableLocation(classTypeMetaData: ClassTypeMetaData): Option[String] =
    classTypeMetaData.getAnnotationValue(HiveExternalTable)

  override protected def reservedKeywords = Seq(
    "ALL",
    "ALTER",
    "AND",
    "AS",
    "BETWEEN",
    "CASE",
    "COLUMN",
    "CREATE",
    "DATABASE",
    "DATE",
    "DELETE",
    "DISTINCT",
    "DROP",
    "ELSE",
    "END",
    "EXISTS",
    "FALSE",
    "FETCH",
    "FULL",
    "FUNCTION",
    "GRANT",
    "GROUP",
    "HAVING",
    "INNER",
    "INSERT",
    "INTO",
    "JOIN",
    "LEFT",
    "NOT",
    "NULL",
    "OR",
    "ORDER",
    "OUTER",
    "SELECT",
    "TABLE",
    "TRUE",
    "UNION",
    "UPDATE",
    "USER",
    "USING",
    "VALUES",
    "WHEN",
    "WHERE"
  )

  override protected def escapeColumnName(columnName: String) =
    s"`$columnName`"
}
