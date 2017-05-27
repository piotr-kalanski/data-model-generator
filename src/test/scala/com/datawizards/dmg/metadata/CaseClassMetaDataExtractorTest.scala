package com.datawizards.dmg.metadata

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CaseClassMetaDataExtractorTest extends FunSuite {
  test("Extract Person MetaData") {
    val expected = CaseClassMetaData(
      packageName = "com.datawizards.dmg.metadata"
      , className = "Person"
      , annotations = Seq(
        AnnotationMetaData(
          name = "com.datawizards.dmg.annotations.table"
          , attributes = Seq(
            AnnotationAttributeMetaData(
              name = "name"
              , value = "PEOPLE"
            )
          )
        )
      )
      , fields = Seq(
        ClassFieldMetaData(
          name = "name"
          , typeName = "String"
          , annotations = Seq(
            AnnotationMetaData(
              name = "com.datawizards.dmg.annotations.column"
              , attributes = Seq(
                AnnotationAttributeMetaData(
                  name = "name"
                  , value = "personName_es"
                ),
                AnnotationAttributeMetaData(
                  name = "dialect"
                  , value = "com.datawizards.dmg.dialects.ElasticsearchDialect"
                )
              )
            ),
            AnnotationMetaData(
              name = "com.datawizards.dmg.annotations.column"
              , attributes = Seq(
                AnnotationAttributeMetaData(
                  name = "name"
                  , value = "person_name"
                )
              )
            ),
            AnnotationMetaData(
              name = "com.datawizards.dmg.metadata.anotherAnnotation"
              , attributes = Seq(
                AnnotationAttributeMetaData(
                  name = "value"
                  , value = "name2"
                )
              )
            )
          )
        ),
        ClassFieldMetaData(
          name = "age"
          , typeName = "Int"
          , annotations = Seq(
            AnnotationMetaData(
              name = "com.datawizards.dmg.annotations.column"
              , attributes = Seq(
                AnnotationAttributeMetaData(
                  name = "name"
                  , value = "age_es"
                ),
                AnnotationAttributeMetaData(
                  name = "dialect"
                  , value = "com.datawizards.dmg.dialects.ElasticsearchDialect"
                )
              )
            )
          )
        ),
        ClassFieldMetaData(
          name = "title"
          , typeName = "String"
          , annotations = Seq()
        )
      )
    )

    val result = CaseClassMetaDataExtractor.extractCaseClassMetaData[Person]()

    assertResult(expected)(result)
  }
}
