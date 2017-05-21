package com.datawizards.dmg.dialects

object H2Dialect extends DatabaseDialect {
  override protected def intType: String = "INT"

  override protected def stringType: String = "VARCHAR"

  override protected def longType: String = "BIGINT"

  override protected def doubleType: String = "DOUBLE"

  override protected def floatType: String = "REAL"

  override protected def shortType: String = "SMALLINT"

  override protected def booleanType: String = "BOOLEAN"

  override protected def byteType: String = "TINYINT"

  override protected def dateType: String = "DATE"

  override protected def timestampType: String = "TIMESTAMP"
}
