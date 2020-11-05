package com.datawizards.dmg.dialects

trait Dialect {
  override def toString: String = this.getClass.getSimpleName.stripSuffix("$")
}


