package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect

import scala.annotation.StaticAnnotation

package object annotations {

  /**
    * @param name custom table name
    * @param dialect if provided then custom name only for this dialect
    */
  final class table(val name: String, val dialect: Dialect) extends StaticAnnotation {
    def this(name: String) = this(name, null)
  }

  /**
    * @param name custom column name
    * @param dialect if provided then custom name only for this dialect
    */
  final class column(val name: String, val dialect: Dialect) extends StaticAnnotation {
    def this(name: String) = this(name, null)
  }

  /**
    * @param value documentation comment
    */
  final class comment(val value: String) extends StaticAnnotation

  /**
    * @param value column length
    */
  final class length(val value: Int) extends StaticAnnotation
}
