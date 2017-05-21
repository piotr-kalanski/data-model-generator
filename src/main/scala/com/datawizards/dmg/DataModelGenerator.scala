package com.datawizards.dmg

import com.datawizards.dmg.dialects.Dialect
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object DataModelGenerator {
  def generate[T: ClassTag: TypeTag](dialect: Dialect): String = {
    val klass = implicitly[ClassTag[T]].runtimeClass
    val table = klass.getSimpleName
    val encoder = ExpressionEncoder[T]

    dialect.generateDataModel(table, encoder.schema)
  }

}
