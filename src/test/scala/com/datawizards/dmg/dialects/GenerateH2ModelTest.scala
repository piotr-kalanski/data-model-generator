package com.datawizards.dmg.dialects

import com.datawizards.dmg.DataModelGenerator
import com.datawizards.dmg.TestModel.{ClassWithAllSimpleTypes, Person, PersonWithCustomName}
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

  test("Custom name") {
    val expected =
      """CREATE TABLE PersonWithCustomName(
        |   personName VARCHAR,
        |   age INT
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[PersonWithCustomName](H2Dialect)
    }
  }

}
