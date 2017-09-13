package com.datawizards.dmg.metadata

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MetaDataExtractorTest extends FunSuite {

  def personTypeMetaData = ClassTypeMetaData(
    packageName = "com.datawizards.dmg.metadata"
    , originalTypeName = "Person"
    , typeName = "Person"
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
        originalFieldName = "name"
        , fieldName = "name"
        , fieldType = StringType
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
        originalFieldName = "age"
        , fieldName = "age"
        , fieldType = IntegerType
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
        originalFieldName = "title"
        , fieldName = "title"
        , fieldType = StringType
        , annotations = Seq()
      )
    )
  )

  test("Extract Person MetaData") {
    val expected = personTypeMetaData
    val result = MetaDataExtractor.extractTypeMetaData[Person]()

    assertResult(expected)(result)
  }

  test("Extract CV MetaData") {
    val expected = ClassTypeMetaData(
      packageName = "com.datawizards.dmg.metadata"
      , originalTypeName = "CV"
      , typeName = "CV"
      , annotations = Seq.empty
      , fields = Seq(
          ClassFieldMetaData(
            originalFieldName = "person"
            , fieldName = "person"
            , fieldType = personTypeMetaData
            , annotations = Seq.empty
          ),
          ClassFieldMetaData(
            originalFieldName = "experience"
            , fieldName = "experience"
            , fieldType = CollectionTypeMetaData(
                elementType = ClassTypeMetaData(
                  packageName = "com.datawizards.dmg.metadata"
                  , originalTypeName = "WorkExperience"
                  , typeName = "WorkExperience"
                  , annotations = Seq.empty
                  , fields = Seq(
                    ClassFieldMetaData(
                      originalFieldName = "start"
                      , fieldName = "start"
                      , fieldType = DateType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      originalFieldName = "end"
                      , fieldName = "end"
                      , fieldType = DateType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      originalFieldName = "jobTitle"
                      , fieldName = "jobTitle"
                      , fieldType = StringType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      originalFieldName = "company"
                      , fieldName = "company"
                      , fieldType = ClassTypeMetaData(
                        packageName = "com.datawizards.dmg.metadata"
                        , originalTypeName = "Company"
                        , typeName = "Company"
                        , annotations = Seq.empty
                        , fields = Seq(
                          ClassFieldMetaData(
                            originalFieldName = "name"
                            , fieldName = "name"
                            , fieldType = StringType
                            , annotations = Seq(
                              AnnotationMetaData(
                                name = "com.datawizards.dmg.annotations.column"
                                , attributes = Seq(
                                  AnnotationAttributeMetaData(
                                    name = "name"
                                    , value = "companyName"
                                  )
                                )
                              )
                            )
                          ),
                          ClassFieldMetaData(
                            originalFieldName = "address"
                            , fieldName = "address"
                            , fieldType = StringType
                            , annotations = Seq.empty
                          ),
                          ClassFieldMetaData(
                            originalFieldName = "industry"
                            , fieldName = "industry"
                            , fieldType = StringType
                            , annotations = Seq.empty
                          )
                        )
                      )
                      , annotations = Seq.empty
                    )
                  )
                )
            )
            , annotations = Seq(
              AnnotationMetaData(
                name = "com.datawizards.dmg.annotations.column"
                , attributes = Seq(
                  AnnotationAttributeMetaData(
                    name = "name"
                    , value = "workExperience"
                  )
                )
              )
            )
          ),
          ClassFieldMetaData(
            originalFieldName = "Wierd chars - / +"
            , fieldName = "Wierd chars - / +"
            , fieldType = StringType
            , annotations = Seq.empty
          )
      )
    )

    val result = MetaDataExtractor.extractTypeMetaData[CV]()

    assertResult(expected)(result)
  }
}
