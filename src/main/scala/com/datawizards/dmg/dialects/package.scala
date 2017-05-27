package com.datawizards.dmg

package object dialects {
  val H2: DatabaseDialect = H2Dialect
  val Hive: DatabaseDialect = HiveDialect
  val Redshift: DatabaseDialect = RedshiftDialect
  val AvroSchema: Dialect = AvroSchemaDialect
  val AvroSchemaRegistry: Dialect = AvroSchemaRegistryDialect
  val Elasticsearch: Dialect = ElasticsearchDialect
  val Java: Dialect = JavaDialect
}
