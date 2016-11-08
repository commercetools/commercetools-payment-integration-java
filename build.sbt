import sbt.Keys._

lazy val jvmSdkVersion = "1.3.0"

/**
 * PROJECT DEFINITIONS
 */

lazy val `commercetools-sunrise-payment` = (project in file("."))
  .configs(IntegrationTest)
  .aggregate(`common`, `payone-adapter`, `nopsp-adapter`)
  .settings(javaUnidocSettings ++ commonSettings ++ commonTestSettings : _*)
  .settings(
      libraryDependencies ++= Seq (
        "com.novocode" % "junit-interface" % "0.11",
        "org.assertj" % "assertj-core" % "3.4.1"
      )
  )
  .dependsOn(`common`, `payone-adapter`, `nopsp-adapter`)

lazy val `common` = project
  .configs(IntegrationTest)
  .settings(commonSettings ++ commonTestSettings : _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.commercetools.sdk.jvm.core" % "commercetools-models" % jvmSdkVersion,
      "com.commercetools.sdk.jvm.core" % "commercetools-java-client" % jvmSdkVersion,
      "com.google.code.findbugs" % "jsr305" % "3.0.0",
      "commons-codec" % "commons-codec" % "1.4"
    )
  )

lazy val `payone-adapter` = project
  .configs(IntegrationTest)
  .settings(commonSettings ++ commonTestSettings : _*)
  .settings(
    description := "Payone specific payment methods."
  )
  .dependsOn(`common`)

lazy val `nopsp-adapter` = project
  .configs(IntegrationTest)
  .settings(commonSettings ++ commonTestSettings : _*)
  .dependsOn(`common`)

/**
 * COMMON SETTINGS
 */

lazy val commonSettings = Seq (
  version      := "0.1-SNAPSHOT",
  organization := "com.commercetools.sunrise.payment",
  organizationName := "commercetools GmbH",
  organizationHomepage := Some(url("https://commercetools.com/")),
  description := "The commercetools java payment project intend is to make payment integration easy",
  licenses += "Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),

  publishMavenStyle := true,
  crossPaths := false,
  publishArtifact in Test := false,

  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },

  // for "publish" task $NEXUS_USER and $NEXUS_PASS environment variables must be set
  credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org",
                              System.getenv("NEXUS_USER"), System.getenv("NEXUS_PASS")),

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
    "org.assertj" % "assertj-core" % "3.4.1" % scopes,
    "org.mockito" % "mockito-all" % "1.9.5" % scopes
  )
)
