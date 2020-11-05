package com.datawizards.dmg.dialects
import com.datawizards.dmg.generator.DatabaseGenerator
import com.datawizards.dmg.metadata._

object H2Dialect extends DatabaseGenerator with Dialect {

  def getSupportedDialect(): Dialect = this

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

  override def binaryType: String = "BINARY"

  override def bigDecimalType: String = "DECIMAL(38,18)"

  override def bigIntegerType: String = "BIGINT"

  override def generateArrayTypeExpression(elementTypeExpression: String): String = "ARRAY"

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String = "OTHER"

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String = {
    log.warn("H2 doesn't support Map type. Column converted to VARCHAR")
    "VARCHAR"
  }

  override protected def fieldAdditionalExpressions(f: ClassFieldMetaData): String =
    notNullExpression(f) +
    commentExpression(f)

  override protected def additionalTableProperties(classTypeMetaData: ClassTypeMetaData): String = ""

  override protected def additionalTableExpressions(classTypeMetaData: ClassTypeMetaData): String =
    if(comment(classTypeMetaData).isDefined)
      s"""
         |COMMENT ON TABLE ${classTypeMetaData.typeName} IS '${comment(classTypeMetaData).get}';""".stripMargin
    else ""

  private def notNullExpression(f: ClassFieldMetaData): String =
    if(notNull(f)) " NOT NULL" else ""

  private def commentExpression(f: ClassFieldMetaData): String =
    if(comment(f).isEmpty) "" else s" COMMENT '${comment(f).get}'"

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
    s""""$columnName""""
}
