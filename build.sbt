lazy val root = (project in file("."))
  .settings(
    organization := "com.kubukoz",
    name := "repro",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % "0.18.11",
      "org.http4s" %% "http4s-circe" % "0.18.11",
      "org.http4s" %% "http4s-dsl" % "0.18.11",
      "io.circe" %% "circe-generic" % "0.9.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "io.kamon" %% "kamon-core" % "1.1.2",
      "io.kamon" %% "kamon-http4s" % "1.0.7",
      "io.kamon" %% "kamon-jdbc" % "1.0.2",
      "io.kamon" %% "kamon-zipkin" % "1.0.0",
      "io.monix" %% "monix" % "3.0.0-RC1",
      "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
      "org.postgresql" % "postgresql" % "42.2.2",
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "com.github.pureconfig" %% "pureconfig" % "0.9.1"
    )
  )
