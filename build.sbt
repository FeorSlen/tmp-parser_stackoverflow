val scala3Version = "3.3.3"
val zioVersion    = "2.1.6"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "stackoverflow-parser",
    version      := "0.1.0",
    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio"      % zioVersion,
      "dev.zio" %% "zio-http" % "3.0.1",
      "dev.zio" %% "zio-json" % "0.7.1",
    ),

    assembly / mainClass       := Some("Main"),
    assembly / assemblyJarName := "stackoverflow-parser.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "services", _*) => MergeStrategy.concat
      case PathList("META-INF", _*)             => MergeStrategy.discard
      case "reference.conf"                     => MergeStrategy.concat
      case _                                    => MergeStrategy.first
    },
  )
