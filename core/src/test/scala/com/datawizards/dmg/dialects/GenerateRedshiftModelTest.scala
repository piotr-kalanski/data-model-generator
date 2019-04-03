package com.datawizards.dmg.dialects

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateRedshiftModelTest extends DataModelGeneratorBaseTest {

  test("Simple model") {
    val expected =
      """CREATE TABLE Person(
        |   name VARCHAR,
        |   age INTEGER
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Person](RedshiftDialect)
    }
  }

  test("ClassWithAllSimpleTypes") {
    val expected =
      """CREATE TABLE ClassWithAllSimpleTypes(
        |   strVal VARCHAR,
        |   intVal INTEGER,
        |   longVal BIGINT,
        |   doubleVal DOUBLE PRECISION,
        |   floatVal REAL,
        |   shortVal SMALLINT,
        |   booleanVal BOOLEAN,
        |   byteVal SMALLINT,
        |   dateVal DATE,
        |   timestampVal TIMESTAMP
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithAllSimpleTypes](RedshiftDialect)
    }
  }

  test("Table and column comment") {
    val expected =
      """CREATE TABLE PersonWithComments(
        |   name VARCHAR,
        |   age INTEGER
        |);
        |COMMENT ON TABLE PersonWithComments IS 'People data';
        |COMMENT ON COLUMN PersonWithComments.name IS 'Person name';""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithComments](RedshiftDialect)
    }
  }

  test("Column length") {
    val expected =
      """CREATE TABLE PersonWithCustomLength(
        |   name VARCHAR(1000),
        |   age INTEGER
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithCustomLength](RedshiftDialect)
    }
  }

  test("Array type") {
    val expected =
      """CREATE TABLE CV(
        |   skills VARCHAR,
        |   grades VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[CV](RedshiftDialect)
    }
  }

  test("Nested array type") {
    val expected =
      """CREATE TABLE NestedArray(
        |   nested VARCHAR,
        |   nested3 VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[NestedArray](RedshiftDialect)
    }
  }

  test("Struct types") {
    val expected =
      """CREATE TABLE Book(
        |   title VARCHAR,
        |   year INTEGER,
        |   owner VARCHAR,
        |   authors VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[Book](RedshiftDialect)
    }
  }

  test("Map type") {
    val expected =
      """CREATE TABLE ClassWithMap(
        |   map VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithMap](RedshiftDialect)
    }
  }

  test("ClassWithDash") {
    val expected =
      """CREATE TABLE ClassWithDash(
        |   "add-id" VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithDash](RedshiftDialect)
    }
  }

  test("reserverd keywords") {
    val expected =
      """CREATE TABLE ClassWithReservedKeywords(
        |   "select" VARCHAR,
        |   "where" VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithReservedKeywords](RedshiftDialect)
    }
  }

  test("ClassWithArrayByte") {
    val expected =
      """CREATE TABLE ClassWithArrayByte(
        |   arr VARCHAR
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithArrayByte](RedshiftDialect)
    }
  }

  test("ClassWithBigInteger") {
    val expected =
      """CREATE TABLE ClassWithBigInteger(
        |   n1 BIGINT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigInteger](RedshiftDialect)
    }
  }

  test("ClassWithBigDecimal") {
    val expected =
      """CREATE TABLE ClassWithBigDecimal(
        |   n1 DECIMAL(38,18)
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[ClassWithBigDecimal](RedshiftDialect)
    }
  }
}
