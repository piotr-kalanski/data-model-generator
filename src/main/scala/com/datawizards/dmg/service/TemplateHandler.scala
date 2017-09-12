package com.datawizards.dmg.service

object TemplateHandler {
  def inflate(template: String, variables: Map[String, String]): String = {
    val re = "\\$\\{.*?\\}".r
    var result = template
    for(k <- re.findAllIn(template))
      result = result.replace(k, variables(k.substring(2, k.length-1)))
    result
  }
}
