package com.datawizards.dmg

import com.datawizards.dmg.TestModel.Person
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateH2ModelTest extends FunSuite {
  test("Person model") {
    val expected =
      """CREATE TABLE Person(
        |   name VARCHAR,
        |   age INT
        |);""".stripMargin

    assertResult(expected) {
      DataModelGenerator.generate[Person](Dialect.H2)
    }
  }
}
