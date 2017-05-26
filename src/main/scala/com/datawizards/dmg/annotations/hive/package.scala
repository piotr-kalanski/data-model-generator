package com.datawizards.dmg.annotations

import scala.annotation.StaticAnnotation

package object hive {

  /**
    * Generated Hive DLL should be EXTERNAL table
    *
    * @param location location of data
    */
  final class hiveExternalTable(val location: String) extends StaticAnnotation

  /**
    * STORED AS X clause
    *
    * @param format format of data e.g. PARQUET
    */
  final class hiveStoredAs(val format: String) extends StaticAnnotation
}
