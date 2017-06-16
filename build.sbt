name := "data-model-generator"

organization := "com.github.piotr-kalanski"

version := "0.5.0-SNAPSHOT"

scalaVersion := "2.11.8"

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/piotr-kalanski/data-model-generator"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/piotr-kalanski/data-model-generator"),
    "scm:git:ssh://github.com/piotr-kalanski/data-model-generator.git"
  )
)

developers := List(
  Developer(
    id    = "kalan",
    name  = "Piotr Kalanski",
    email = "piotr.kalanski@gmail.com",
    url   = url("https://github.com/piotr-kalanski")
  )
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "log4j" % "log4j" % "1.2.17",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.json4s" %% "json4s-native" % "3.5.2",
  "com.github.piotr-kalanski" %% "es-client" % "0.1.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "junit" % "junit" % "4.10" % "test"
)

coverageExcludedPackages := "com.datawizards.dmg.examples.*"

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
