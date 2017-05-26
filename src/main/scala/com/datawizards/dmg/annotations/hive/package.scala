package com.datawizards.dmg.annotations

import scala.annotation.StaticAnnotation

package object hive {

  /**
    * Generated Hive DLL should be EXTERNAL table
    *
    * @param location location of data
    */
  final class hiveExternalTable(val location: String) extends StaticAnnotation
}
