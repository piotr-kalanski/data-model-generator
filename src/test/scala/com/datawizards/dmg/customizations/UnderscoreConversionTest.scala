package com.datawizards.dmg.customizations

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.dialects.{H2Dialect, HiveDialect, MySQLDialect, RedshiftDialect}
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UnderscoreConversionTest extends DataModelGeneratorBaseTest {

  test("Underscore conversion - H2") {
    val expected =
      """CREATE TABLE person_with_underscore_with_multiple_names(
        |   person_name VARCHAR,
        |   person_age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithUnderscoreWithMultipleNames](H2Dialect)
    }
  }

  test("Underscore conversion - Hive") {
    val expected =
      """CREATE TABLE PEOPLE(
        |   name STRING,
        |   person_age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithUnderscoreWithMultipleNames](HiveDialect)
    }
  }

  test("Underscore conversion - Redshift") {
    val expected =
      """CREATE TABLE person_with_underscore_with_multiple_names(
        |   name VARCHAR,
        |   age INTEGER
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithUnderscoreWithMultipleNames](RedshiftDialect)
    }
  }

  test("Underscore conversion - MySQL") {
    val expected =
      """CREATE TABLE PersonWithUnderscoreWithMultipleNames(
        |   personName VARCHAR,
        |   personAge INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      DataModelGenerator.generate[PersonWithUnderscoreWithMultipleNames](MySQLDialect)
    }
  }

}
