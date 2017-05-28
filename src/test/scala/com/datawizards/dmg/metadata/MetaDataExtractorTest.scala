package com.datawizards.dmg.metadata

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MetaDataExtractorTest extends FunSuite {

  def personTypeMetaData = ClassTypeMetaData(
    packageName = "com.datawizards.dmg.metadata"
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
        fieldName = "name"
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
        fieldName = "age"
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
        fieldName = "title"
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
      , typeName = "CV"
      , annotations = Seq.empty
      , fields = Seq(
          ClassFieldMetaData(
            fieldName = "person"
            , fieldType = personTypeMetaData
            , annotations = Seq.empty
          ),
          ClassFieldMetaData(
            fieldName = "experience"
            , fieldType = CollectionTypeMetaData(
                elementType = ClassTypeMetaData(
                  packageName = "com.datawizards.dmg.metadata"
                  , typeName = "WorkExperience"
                  , annotations = Seq.empty
                  , fields = Seq(
                    ClassFieldMetaData(
                      fieldName = "start"
                      , fieldType = DateType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      fieldName = "end"
                      , fieldType = DateType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      fieldName = "jobTitle"
                      , fieldType = StringType
                      , annotations = Seq.empty
                    ),
                    ClassFieldMetaData(
                      fieldName = "company"
                      , fieldType = ClassTypeMetaData(
                        packageName = "com.datawizards.dmg.metadata"
                        , typeName = "Company"
                        , annotations = Seq.empty
                        , fields = Seq(
                          ClassFieldMetaData(
                            fieldName = "name"
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
                            fieldName = "address"
                            , fieldType = StringType
                            , annotations = Seq.empty
                          ),
                          ClassFieldMetaData(
                            fieldName = "industry"
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
          )
      )
    )

    val result = MetaDataExtractor.extractTypeMetaData[CV]()

    assertResult(expected)(result)
  }
}
