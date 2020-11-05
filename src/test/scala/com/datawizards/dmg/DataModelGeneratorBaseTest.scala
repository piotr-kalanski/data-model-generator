package com.datawizards.dmg

import org.scalatest.{FunSuite, Matchers}

trait DataModelGeneratorBaseTest extends FunSuite with Matchers {
  def assertResultIgnoringNewLines(expected: String)(result: String): Unit =
    stripNewLines(result) should equal(stripNewLines(expected))

  private def stripNewLines(v: String): String = v.replace("\n", "").replace("\r", "")

  def alignWhitespace(v: String): String = v.replaceAll("\\s+", " ")
}
