package com.datawizards.dmg

import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person}
import com.datawizards.dmg.dialects.H2Dialect
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateH2ModelTest extends FunSuite {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name VARCHAR,
        |   age INT
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[Person](H2Dialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """CREATE TABLE ClassWithAllSimpleTypes(
        |   strVal VARCHAR,
        |   intVal INT,
        |   longVal BIGINT,
        |   doubleVal DOUBLE,
        |   floatVal REAL,
        |   shortVal SMALLINT,
        |   booleanVal BOOLEAN,
        |   byteVal TINYINT,
        |   dateVal DATE,
        |   timestampVal TIMESTAMP
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](H2Dialect)
    }
  }

}
