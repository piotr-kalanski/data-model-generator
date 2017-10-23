package com.datawizards.dmg

import com.datawizards.dmg.metadata.{MetaDataExtractor, PersonFull}
import org.scalatest.FunSuite

class ExtractClassMetaDataForDialectTest extends FunSuite{

  test("PersonFull - Elasticsearch") {
    val a = MetaDataExtractor.extractClassMetaDataForDialect[PersonFull](Some(com.datawizards.dmg.dialects.ElasticsearchDialect))

    println(a)
  }
}
