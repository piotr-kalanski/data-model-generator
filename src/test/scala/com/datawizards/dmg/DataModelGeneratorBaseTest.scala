package com.datawizards.dmg

import org.scalatest.{FunSuite, Matchers}

trait DataModelGeneratorBaseTest extends FunSuite with Matchers {
  def assertResultIgnoringNewLines(expected: String)(result: String): Unit =
    expected.replace("\n", "").replace("\r", "") should equal(result.replace("\n", "").replace("\r", ""))
}
