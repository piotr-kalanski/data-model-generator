package com.datawizards.dmg

object Dialect {
  object H2 extends DatabaseDialect
  object Hive extends DatabaseDialect
  object MySQL extends DatabaseDialect
  object Redshift extends DatabaseDialect
  object AvroSchema extends Dialect
  object ElasticsearchMapping extends Dialect
}

trait Dialect {
  def mapScalaType(klass: Class[_]): String = {
    val JInteger = classOf[Int]
    val JString = classOf[String]

    klass match {
      case JInteger => "INT"
      case JString => "VARCHAR"
      case _ => throw new Exception("Not supported class: " + klass.getName)
    }
  }

}

trait DatabaseDialect extends Dialect
