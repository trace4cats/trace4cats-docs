lazy val commonSettings = Seq(
  libraryDependencies += compilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.0").cross(CrossVersion.patch)),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) => compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1") :: Nil
      case _ => Nil
    }
  },
  Compile / compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  scalacOptions := {
    val opts = scalacOptions.value :+ "-Wconf:src=src_managed/.*:s,any:wv"

    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => opts.filterNot(Set("-Xfatal-warnings"))
      case _ => opts
    }
  },
  Test / fork := true,
  resolvers += Resolver.sonatypeRepo("releases"),
  ThisBuild / evictionErrorLevel := Level.Warn,
)

lazy val noPublishSettings =
  commonSettings ++ Seq(publish := {}, publishArtifact := false, publishTo := None, publish / skip := true)

lazy val root = (project in file("."))
  .settings(noPublishSettings)
  .settings(name := "Trace4Cats Docs")
  .aggregate(example)

lazy val example = (project in file("modules/example"))
  .settings(noPublishSettings)
  .settings(
    name := "trace4cats-example",
    libraryDependencies ++= Seq(
      Dependencies.trace4catsInject,
      Dependencies.trace4catsFiltering,
      Dependencies.trace4catsDynamicSampling,
      Dependencies.trace4catsFs2,
      Dependencies.trace4catsLogExporter,
      Dependencies.trace4catsRateSampling,
      Dependencies.trace4catsTailSampling,
      Dependencies.trace4catsAvro,
      Dependencies.trace4catsAvroExporter,
      Dependencies.trace4catsAvroServer,
      Dependencies.trace4catsJaeger,
      Dependencies.trace4catsCloudtraceGrpc,
      Dependencies.trace4catsCloudtraceHttp,
      Dependencies.trace4catsOtlpGrpc,
      Dependencies.trace4catsOtlpHttp,
      Dependencies.trace4catsOtelJaeger,
      Dependencies.trace4catsDatadog,
      Dependencies.trace4catsNewrelic,
      Dependencies.trace4catsZipkin,
      Dependencies.trace4catsZio,
      Dependencies.trace4catsNatchez,
      Dependencies.trace4catsSttpClient3,
      Dependencies.trace4catsTapir,
      Dependencies.trace4catsHttp4sClient,
      Dependencies.trace4catsHttp4sServer,
      Dependencies.trace4catsTailSamplingCache,
      Dependencies.trace4catsTailSamplingRedis,
      Dependencies.trace4catsDynamicSamplingHttp4s,
      Dependencies.trace4catsDynamicSamplingHttpServer,
      Dependencies.catsEffect,
      Dependencies.http4sBlazeClient,
      Dependencies.http4sBlazeServer,
      Dependencies.sttpClient3Http4s
    )
  )
