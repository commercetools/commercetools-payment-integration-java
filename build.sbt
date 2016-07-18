import sbt.Keys._

name := "commercetools-sunrise-payment"

organization := "com.commercetools.sunrise.payment"

lazy val jvmSdkVersion = "1.1.0"

/**
 * PROJECT DEFINITIONS
 */

lazy val `commercetools-sunrise-payment` = (project in file("."))
  .aggregate(`common`, `payone-adapter`)
  .settings(javaUnidocSettings ++ commonSettings : _*)

lazy val `common` = project
  .configs(IntegrationTest)
  .settings(commonSettings ++ commonTestSettings : _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.commercetools.sdk.jvm.core" % "commercetools-models" % jvmSdkVersion
    )
  )

lazy val `payone-adapter` = project
  .configs(IntegrationTest)
  .settings(commonSettings ++ commonTestSettings : _*)
  .dependsOn(`common`)


/**
 * COMMON SETTINGS
 */

lazy val commonSettings = Seq (
  scalaVersion := "2.11.8",
  javacOptions in (Compile, doc) := Seq("-quiet", "-notimestamp"),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
)

/**
 * TEST SETTINGS
 */
lazy val commonTestSettings = itBaseTestSettings ++ configCommonTestSettings("test,it")

lazy val itBaseTestSettings = Defaults.itSettings ++ configTestDirs(IntegrationTest, "it")

def configTestDirs(config: Configuration, folderName: String) = {
  val srcFolder: String = "src"
  Seq(
    javaSource in config := baseDirectory.value / srcFolder / folderName,
    scalaSource in config := baseDirectory.value / srcFolder / folderName,
    resourceDirectory in config := baseDirectory.value / srcFolder / s"$folderName/resources"
  )
}

def configCommonTestSettings(scopes: String) = Seq(
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  // parallelExecution := false, Enable if necessary
  libraryDependencies ++= Seq (
    "com.novocode" % "junit-interface" % "0.11" % scopes,
    "org.assertj" % "assertj-core" % "3.4.1" % scopes
  )
)