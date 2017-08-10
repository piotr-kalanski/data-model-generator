package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

object MySQLDialect extends DatabaseDialect {
  override def intType: String = "INT"

  override def stringType: String = "VARCHAR"

  override def longType: String = "BIGINT"

  override def doubleType: String = "DOUBLE"

  override def floatType: String = "FLOAT"

  override def shortType: String = "SMALLINT"

  override def booleanType: String = "BOOLEAN"

  override def byteType: String = "SMALLINT"

  override def dateType: String = "DATE"

  override def timestampType: String = "TIMESTAMP"

  override def binaryType: String = "BINARY"

  override def generateArrayTypeExpression(elementTypeExpression: String): String = {
    log.warn("MySQL doesn't support ARRAY type. Column converted to JSON.")
    "JSON"
  }

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String = {
    log.warn("MySQL doesn't support Struct type. Column converted to JSON")
    "JSON"
  }

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String = {
    log.warn("MySQL doesn't support Map type. Column converted to JSON")
    "JSON"
  }

  override def toString: String = "MySQLDialect"

  override protected def fieldAdditionalExpressions(f: ClassFieldMetaData): String =
    notNullExpression(f) +
    commentExpression(f)

  override protected def additionalTableProperties(classTypeMetaData: ClassTypeMetaData): String =
    if(comment(classTypeMetaData).isDefined)
      s"\nCOMMENT = '${comment(classTypeMetaData).get}'"
    else ""

  override protected def additionalTableExpressions(classTypeMetaData: ClassTypeMetaData): String = ""

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
