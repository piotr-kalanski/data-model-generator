package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations.column

case class Company(
  @column(name="companyName")
  name: String,
  address: String,
  industry: String
)
