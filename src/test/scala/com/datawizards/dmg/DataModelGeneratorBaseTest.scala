package com.datawizards.dmg

import org.scalatest.FunSuite

trait DataModelGeneratorBaseTest extends FunSuite {
  def assertResultIgnoringNewLines(expected: String)(result: String): Unit =
    assertResult(expected.replace("\n", "").replace("\r", ""))(result.replace("\n", "").replace("\r", ""))
}
