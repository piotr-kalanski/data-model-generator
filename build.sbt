ThisBuild / organization := "com.github.piotr-kalanski"
ThisBuild / version := "0.7.7"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.8")
ThisBuild / scalaVersion := crossScalaVersions.value.head

ThisBuild / licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

ThisBuild / homepage := Some(url("https://github.com/piotr-kalanski/data-model-generator"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/piotr-kalanski/data-model-generator"),
    "scm:git:ssh://github.com/piotr-kalanski/data-model-generator.git"
  )
)

val testDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.0.7" % "test",
  "junit" % "junit" % "4.10" % "test",
)

ThisBuild / developers := List(
  Developer(
    id    = "kalan",
    name  = "Piotr Kalanski",
    email = "piotr.kalanski@gmail.com",
    url   = url("https://github.com/piotr-kalanski")
  )
)

ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

lazy val core = (project in file("core"))
    .settings(
      name := "data-model-generator",
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "log4j" % "log4j" % "1.2.17",
        "org.scalaj" %% "scalaj-http" % "2.4.1",
        "org.json4s" %% "json4s-native" % "3.6.5",
        "org.json4s" %% "json4s-jackson" % "3.6.5",
      ) ++ testDeps,
      coverageExcludedPackages := "com.datawizards.dmg.examples.*"
    )

lazy val elasticsearch = (project in file("elasticsearch"))
  .dependsOn(core % "test->test;compile->compile")
  .settings(
    name := "data-model-generator-elasticsearch",
    scalaVersion := "2.11.12",
    libraryDependencies ++= Seq(
      "com.github.piotr-kalanski" %% "es-client" % "0.2.1",
    ) ++ testDeps
  )
