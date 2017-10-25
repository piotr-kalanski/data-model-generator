package com.datawizards.dmg

import com.datawizards.dmg.metadata.{MetaDataExtractor, PersonFull}
import org.scalatest.{FunSuite, Matchers}

class ExtractClassMetaDataForDialectTest extends FunSuite with Matchers {

  test("PersonFull - Elasticsearch") {
    val a = MetaDataExtractor.extractClassMetaDataForDialect[PersonFull](Some(com.datawizards.dmg.dialects.ElasticsearchDialect))

    println(a)

    a.typeName should equal("person_full_es")
    a.fields.size should equal (3)
    a.fields.toList(0).fieldName should equal("personName")
    a.fields.toList(1).fieldName should equal("personAge")
    a.fields.toList(2).fieldName should equal("personTitle")
  }

  test("PersonFull - MySQL") {
    val a = MetaDataExtractor.extractClassMetaDataForDialect[PersonFull](Some(com.datawizards.dmg.dialects.MySQLDialect))


    println(a)

    a.typeName should equal("person_full")
    a.fields.toList(0).fieldName should equal("person_name")
    a.fields.toList(1).fieldName should equal("person_age")
    a.fields.toList(2).fieldName should equal("title")
  }


  test("PersonFull - None dialect") {
    val a = MetaDataExtractor.extractClassMetaDataForDialect[PersonFull](None)

    println(a)

    a.typeName should equal("person_full")
    a.fields.toList(0).fieldName should equal("person_name")
    a.fields.toList(1).fieldName should equal("person_age")
    a.fields.toList(2).fieldName should equal("title")
  }
}
