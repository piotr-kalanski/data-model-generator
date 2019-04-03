package com.datawizards.dmg.dialects

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateMySQLModelTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name VARCHAR,
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](MySQLDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """CREATE TABLE ClassWithAllSimpleTypes(
        |   strVal VARCHAR,
        |   intVal INT,
        |   longVal BIGINT,
        |   doubleVal DOUBLE,
        |   floatVal FLOAT,
        |   shortVal SMALLINT,
        |   booleanVal BOOLEAN,
        |   byteVal SMALLINT,
        |   dateVal DATE,
        |   timestampVal TIMESTAMP
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](MySQLDialect)
    }
  }

  test("Table and column comment") {
    val expected =
      """CREATE TABLE PersonWithComments(
        |   name VARCHAR COMMENT 'Person name',
        |   age INT
        |)
        |COMMENT = 'People data';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithComments](MySQLDialect)
    }
  }

  test("Column length") {
    val expected =
      """CREATE TABLE PersonWithCustomLength(
        |   name VARCHAR(1000),
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithCustomLength](MySQLDialect)
    }
  }

  test("Array type") {
    val expected =
      """CREATE TABLE CV(
        |   skills JSON,
        |   grades JSON
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](MySQLDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """CREATE TABLE NestedArray(
        |   nested JSON,
        |   nested3 JSON
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](MySQLDialect)
    }
  }

  test("Struct types") {
    val expected =
      """CREATE TABLE Book(
        |   title VARCHAR,
        |   year INT,
        |   owner JSON,
        |   authors JSON
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](MySQLDialect)
    }
  }

  test("Map type") {
    val expected =
      """CREATE TABLE ClassWithMap(
        |   map JSON
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](MySQLDialect)
    }
  }

  test("ClassWithDash") {
    val expected =
      """CREATE TABLE ClassWithDash(
        |   "add-id" VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithDash](MySQLDialect)
    }
  }

  test("reserverd keywords") {
    val expected =
      """CREATE TABLE ClassWithReservedKeywords(
        |   "select" VARCHAR,
        |   "where" VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithReservedKeywords](MySQLDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """CREATE TABLE ClassWithArrayByte(
        |   arr BINARY
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](MySQLDialect)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """CREATE TABLE ClassWithBigInteger(
        |   n1 BIGINT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](MySQLDialect)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """CREATE TABLE ClassWithBigDecimal(
        |   n1 DECIMAL(38,18)
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](MySQLDialect)
    }
  }
}
