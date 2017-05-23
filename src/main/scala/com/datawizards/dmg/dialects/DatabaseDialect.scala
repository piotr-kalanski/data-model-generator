package com.datawizards.dmg.dialects

import com.datawizards.dmg.model.{ArrayFieldType, ClassMetaData, FieldType, PrimitiveFieldType}

trait DatabaseDialect extends Dialect {

  override def generateDataModel(classMetaData: ClassMetaData): String = {
    val columnsExpression = generateColumnsExpression(classMetaData)

    s"CREATE TABLE ${classMetaData.className}(\n" +
      s"   $columnsExpression" +
      s"\n)${additionalTableProperties(classMetaData)};${additionalTableExpressions(classMetaData)}"
  }

  private def generateColumnsExpression(classMetaData: ClassMetaData): String =
    classMetaData
      .fields
      .map(f =>
        f.name + " " + getFieldType(f.targetType) +
        (if(f.length.isEmpty) "" else s"(${f.length.get})") +
        (if(f.comment.isEmpty) "" else s" COMMENT '${f.comment.get}'")
      ).mkString(",\n   ")

  protected def additionalTableProperties(classMetaData: ClassMetaData): String

  protected def additionalTableExpressions(classMetaData: ClassMetaData): String

  protected def getFieldType(fieldType: FieldType): String = fieldType match {
    case p:PrimitiveFieldType => p.name
    case a:ArrayFieldType => getArrayType(a)
  }

  protected def getArrayType(a: ArrayFieldType): String

}
