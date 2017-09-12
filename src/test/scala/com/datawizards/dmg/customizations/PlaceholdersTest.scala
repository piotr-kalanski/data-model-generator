package com.datawizards.dmg.customizations

import com.datawizards.dmg.TestModel.PersonWithPlaceholderVariables
import com.datawizards.dmg.dialects.H2Dialect
import com.datawizards.dmg.service.TemplateHandler
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlaceholdersTest extends DataModelGeneratorBaseTest {

  test("Placeholder variables") {
    val expected =
      """CREATE TABLE SCHEMA.PEOPLE(
        |   name VARCHAR COMMENT 'Person first and last name',
        |   age INT
        |);""".stripMargin

    assertResultIgnoringNewLines(expected) {
      TemplateHandler.inflate(DataModelGenerator.generate[PersonWithPlaceholderVariables](H2Dialect), Map("table_name" -> "SCHEMA.PEOPLE", "name_comment" -> "first and last name"))
    }
  }

}
