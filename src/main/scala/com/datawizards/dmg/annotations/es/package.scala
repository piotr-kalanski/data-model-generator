package com.datawizards.dmg.annotations

import scala.annotation.StaticAnnotation

/**
  * Elasticsearch mapping dedicated annotations
  */
package object es {

  /**
    * The index option controls whether field values are indexed.
    * [[https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-index.html]]
    *
    */
  final class esIndex(value: String) extends StaticAnnotation

  /**
    * Date format
    * [[https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-date-format.html]]
    *
    */
  final class esFormat(value: String) extends StaticAnnotation

  /**
    * Index settings
    * [[https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules.html]]
    *
    */
  final class esSetting(key: String, value: Any) extends StaticAnnotation

}
