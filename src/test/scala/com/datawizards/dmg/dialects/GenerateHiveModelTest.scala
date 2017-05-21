package com.datawizards.dmg.dialects

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateHiveModelTest extends FunSuite {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name STRING,
        |   age INT
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[Person](HiveDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """CREATE TABLE ClassWithAllSimpleTypes(
        |   strVal STRING,
        |   intVal INT,
        |   longVal BIGINT,
        |   doubleVal DOUBLE,
        |   floatVal FLOAT,
        |   shortVal SMALLINT,
        |   booleanVal BOOLEAN,
        |   byteVal TINYINT,
        |   dateVal DATE,
        |   timestampVal TIMESTAMP
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](HiveDialect)
    }
  }

}
