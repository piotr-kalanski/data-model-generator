package com.datawizards.dmg

import scala.annotation.StaticAnnotation

package object annotations {
  final class table(val name: String) extends StaticAnnotation
  final class column(val name: String, val context: String) extends StaticAnnotation {
    def this(name: String) = this(name, "default")
  }
  final class comment(val value: String) extends StaticAnnotation
  final class length(val value: Int) extends StaticAnnotation
}
