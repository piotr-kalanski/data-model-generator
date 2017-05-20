package com.datawizards.dmg

import scala.reflect.ClassTag

object DataModelGenerator {
  def generate[T: ClassTag](dialect: Dialect): String = {
    val klass = implicitly[ClassTag[T]].runtimeClass
    val table = klass.getSimpleName
    val columns = klass
      .getDeclaredFields
      .map(f => f.getName -> dialect.mapScalaType(f.getType))

    val columnsExpressions = columns.map(p => p._1 + " " + p._2).mkString(",\n   ")
    s"CREATE TABLE $table(\n" +
      s"   $columnsExpressions" +
      s"\n);"
  }

}
