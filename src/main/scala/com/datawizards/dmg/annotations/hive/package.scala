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

  /**
    * ROW FORMAT SERDE X clause
    *
    * @param format row format of data e.g. org.apache.hadoop.hive.serde2.avro.AvroSerDe, org.apache.hive.hcatalog.data.JsonSerDe
    */
  final class hiveRowFormatSerde(val format: String) extends StaticAnnotation
}
