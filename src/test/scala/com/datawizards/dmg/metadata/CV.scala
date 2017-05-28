package com.datawizards.dmg.metadata

import com.datawizards.dmg.annotations.column

case class CV(
  person: Person,
  @column(name="workExperience")
  experience: Iterable[WorkExperience]
)
