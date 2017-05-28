package com.datawizards.dmg.metadata

import java.util.Date

case class WorkExperience(
  start: Date,
  end: Date,
  jobTitle: String,
  company: Company
)
