val Http4sVersion = "0.18.16"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"
val SlickVersion = "3.2.3"
val Slf4jNop = "1.6.4"
val slickCats = "0.7.1.1"
val h2Version = "1.4.192"
val monixVersion = "3.0.0-RC2-d0feeba"

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.mikla",
    name := "http4s-auth-ft",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "com.rms.miu" %% "slick-cats" % slickCats,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "io.monix" %% "monix" % monixVersion,
      "org.slf4j" % "slf4j-nop" % Slf4jNop,
      "com.h2database" % "h2" % h2Version
    )
  )

