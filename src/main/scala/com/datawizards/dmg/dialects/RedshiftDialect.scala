package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

object RedshiftDialect extends DatabaseDialect {
  override def intType: String = "INTEGER"

  override def stringType: String = "VARCHAR"

  override def longType: String = "BIGINT"

  override def doubleType: String = "DOUBLE PRECISION"

  override def floatType: String = "REAL"

  override def shortType: String = "SMALLINT"

  override def booleanType: String = "BOOLEAN"

  override def byteType: String = "SMALLINT"

  override def dateType: String = "DATE"

  override def timestampType: String = "TIMESTAMP"

  override def generateArrayTypeExpression(elementTypeExpression: String): String = {
    log.warn("Redshift doesn't support ARRAY type. Column converted to VARCHAR.")
    "VARCHAR"
  }

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String = {
    log.warn("Redshift doesn't support Struct type. Column converted to VARCHAR.")
    "VARCHAR"
  }

  override def toString: String = "RedshiftDialect"

  override protected def fieldAdditionalExpressions(f: ClassFieldMetaData): String =
    notNullExpression(f)

  override protected def additionalTableProperties(classTypeMetaData: ClassTypeMetaData): String = ""

  override protected def additionalTableExpressions(classTypeMetaData: ClassTypeMetaData): String =
    (
      if(comment(classTypeMetaData).isDefined)
        s"\nCOMMENT ON TABLE ${classTypeMetaData.typeName} IS '${comment(classTypeMetaData).get}';"
      else ""
    ) + classTypeMetaData.fields.map(f =>
      if(comment(f).isDefined)
        s"\nCOMMENT ON COLUMN ${classTypeMetaData.typeName}.${f.fieldName} IS '${comment(f).get}';"
      else ""
    ).mkString("")

  private def notNullExpression(f: ClassFieldMetaData): String =
    if(notNull(f)) " NOT NULL" else ""
}
