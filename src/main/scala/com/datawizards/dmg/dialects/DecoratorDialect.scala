package com.datawizards.dmg.dialects

import com.datawizards.dmg.metadata._

abstract class DecoratorDialect(dialect: Dialect) extends Dialect {

  override def intType: String = dialect.intType

  override def stringType: String = dialect.stringType

  override def longType: String = dialect.longType

  override def doubleType: String = dialect.doubleType

  override def floatType: String = dialect.floatType

  override def shortType: String = dialect.shortType

  override def booleanType: String = dialect.booleanType

  override def byteType: String = dialect.byteType

  override def dateType: String = dialect.dateType

  override def timestampType: String = dialect.timestampType

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    dialect.generateClassFieldExpression(f, typeExpression, level)

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    decorate(dialect.generateDataModel(classTypeMetaData, fieldsExpressions))

  protected def decorate(dataModel: String): String

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    dialect.generateArrayTypeExpression(elementTypeExpression)

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String =
    dialect.generateMapTypeExpression(keyExpression, valueExpression)

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    dialect.generateClassTypeExpression(classTypeMetaData, fieldNamesWithExpressions)
}
