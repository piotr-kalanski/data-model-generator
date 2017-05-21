package com.datawizards.dmg.dialects

import com.datawizards.dmg.ClassMetaData

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

  override def generateDataModel(classMetaData: ClassMetaData): String =
    decorate(dialect.generateDataModel(classMetaData))

  protected def decorate(dataModel: String): String
}
