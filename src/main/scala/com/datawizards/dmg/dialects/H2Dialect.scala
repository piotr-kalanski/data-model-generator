package com.datawizards.dmg.dialects
import com.datawizards.dmg.metadata._

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

  override def generateArrayTypeExpression(elementTypeExpression: String): String = "ARRAY"

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String = "OTHER"

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String = {
    log.warn("H2 doesn't support Map type. Column converted to VARCHAR")
    "VARCHAR"
  }

  override def toString: String = "H2Dialect"

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
}
