package com.datawizards.dmg.dialects

import com.datawizards.dmg.TestModel._
import com.datawizards.dmg.generator.HiveGenerator
import com.datawizards.dmg.metadata.ClassTypeMetaData
import com.datawizards.dmg.{DataModelGenerator, DataModelGeneratorBaseTest}
import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GenerateHiveModelHashTest extends DataModelGeneratorBaseTest {

  test("Simple model - don't create table.") {
    val expected =
      """--Not re-creating table for class Person because it was not modified.
        |--CREATE TABLE Person(
        |--   name STRING,
        |--   age INT
        |--)
        |--TBLPROPERTIES(
        |--   'MODEL_GENERATOR_METADATA_HASH' = '877255039'
        |--);
        |""".stripMargin


    alignWhitespace(DataModelGenerator.generate[Person](new HiveGenerator(){
        override def getHashFromTableDefinition(metadata: ClassTypeMetaData): Option[Long] = {
          println("getHashFromTableDefinition(): " + getClassTypeMetaDataHash(metadata))
          Some(877255039)
        }
      })) should equal(alignWhitespace(expected))

  }

}
