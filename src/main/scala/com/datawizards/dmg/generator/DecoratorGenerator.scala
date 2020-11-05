package com.datawizards.dmg.generator

import com.datawizards.dmg.metadata._

abstract class DecoratorGenerator(generator: Generator) extends Generator {

  override def intType: String = generator.intType

  override def stringType: String = generator.stringType

  override def longType: String = generator.longType

  override def doubleType: String = generator.doubleType

  override def floatType: String = generator.floatType

  override def shortType: String = generator.shortType

  override def booleanType: String = generator.booleanType

  override def byteType: String = generator.byteType

  override def dateType: String = generator.dateType

  override def timestampType: String = generator.timestampType

  override def binaryType: String = generator.binaryType

  override def bigDecimalType: String = generator.bigDecimalType

  override def bigIntegerType: String = generator.bigIntegerType

  override def generateClassFieldExpression(f: ClassFieldMetaData, typeExpression: String, level: Int): String =
    generator.generateClassFieldExpression(f, typeExpression, level)

  override def generateDataModel(classTypeMetaData: ClassTypeMetaData, fieldsExpressions: Iterable[String]): String =
    decorate(generator.generateDataModel(classTypeMetaData, fieldsExpressions))

  protected def decorate(dataModel: String): String

  override def generateArrayTypeExpression(elementTypeExpression: String): String =
    generator.generateArrayTypeExpression(elementTypeExpression)

  override def generateMapTypeExpression(keyExpression: String, valueExpression: String): String =
    generator.generateMapTypeExpression(keyExpression, valueExpression)

  override def generateClassTypeExpression(classTypeMetaData: ClassTypeMetaData, fieldNamesWithExpressions: Iterable[(String, String)]): String =
    generator.generateClassTypeExpression(classTypeMetaData, fieldNamesWithExpressions)
}
