name := "SolarSystemScaleModel"

lazy val root = project.in(file(".")).aggregate(base, integration)

lazy val base = project.in(file("base")).settings(
  scalaVersion := "2.11.6",
  libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.5.2",
  libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
  testFrameworks += new TestFramework("utest.runner.Framework"),
  persistLauncher in Compile := true
).enablePlugins(ScalaJSPlugin)

//This just holds the integration test. It's a separate project because it compiles to java bytecode
//rather than javascript as scalatest and seleniumhq only run on the jvm
//It depends on fastOptJs being run manually as I don't have the dependencies right yet.
lazy val integration = project.in(file("integration")).settings(
  scalaVersion := "2.11.6",
  libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test"
)

