import sbt._

object Dependencies {
  object Versions {
    val scala212 = "2.12.14"
    val scala213 = "2.13.6"

    val trace4cats = "0.12.0-RC2"
    val trace4catsAvro = "0.12.0-RC2"
    val trace4catsCloudtrace = "0.12.0-RC2"
    val trace4catsDatadog = "0.12.0-RC2"
    val trace4catsHttp4s = "0.12.0-RC2"
    val trace4catsJaeger = "0.12.0-RC2"
    val trace4catsNatchez = "0.12.0-RC2"
    val trace4catsNewrelic = "0.12.0-RC2"
    val trace4catsOtel = "0.12.0-RC2"
    val trace4catsSttp = "0.12.0-RC2"
    val trace4catsTailSamplingExtras = "0.12.0-RC2"
    val trace4catsDynamicSamplingExtras = "0.12.0-RC2"
    val trace4catsZio = "0.12.0-RC2"
    val trace4catsZipkin = "0.12.0-RC2"

    val catsEffect = "3.1.1"
    val http4s = "0.23.0-RC1"
    val sttpClient3 = "3.3.11"

    val kindProjector = "0.13.0"
    val betterMonadicFor = "0.3.1"
  }

  lazy val trace4catsDynamicSampling = "io.janstenpickle" %% "trace4cats-dynamic-sampling" % Versions.trace4cats
  lazy val trace4catsFiltering = "io.janstenpickle"       %% "trace4cats-filtering"        % Versions.trace4cats
  lazy val trace4catsFs2 = "io.janstenpickle"             %% "trace4cats-fs2"              % Versions.trace4cats
  lazy val trace4catsInject = "io.janstenpickle"          %% "trace4cats-inject"           % Versions.trace4cats
  lazy val trace4catsLogExporter = "io.janstenpickle"     %% "trace4cats-log-exporter"     % Versions.trace4cats
  lazy val trace4catsMeta = "io.janstenpickle"            %% "trace4cats-meta"             % Versions.trace4cats
  lazy val trace4catsRateSampling = "io.janstenpickle"    %% "trace4cats-rate-sampling"    % Versions.trace4cats
  lazy val trace4catsTailSampling = "io.janstenpickle"    %% "trace4cats-tail-sampling"    % Versions.trace4cats

  lazy val trace4catsAvro = "io.janstenpickle"         %% "trace4cats-avro"          % Versions.trace4catsAvro
  lazy val trace4catsAvroExporter = "io.janstenpickle" %% "trace4cats-avro-exporter" % Versions.trace4catsAvro
  lazy val trace4catsAvroServer = "io.janstenpickle"   %% "trace4cats-avro-server"   % Versions.trace4catsAvro

  lazy val trace4catsJaeger = "io.janstenpickle" %% "trace4cats-jaeger-thrift-exporter" % Versions.trace4catsJaeger

  lazy val trace4catsCloudtraceGrpc =
    "io.janstenpickle" %% "trace4cats-stackdriver-grpc-exporter" % Versions.trace4catsCloudtrace
  lazy val trace4catsCloudtraceHttp =
    "io.janstenpickle" %% "trace4cats-stackdriver-http-exporter" % Versions.trace4catsCloudtrace

  lazy val trace4catsOtlpGrpc =
    "io.janstenpickle" %% "trace4cats-opentelemetry-otlp-grpc-exporter" % Versions.trace4catsOtel
  lazy val trace4catsOtlpHttp =
    "io.janstenpickle" %% "trace4cats-opentelemetry-otlp-http-exporter" % Versions.trace4catsOtel
  lazy val trace4catsOtelJaeger =
    "io.janstenpickle" %% "trace4cats-opentelemetry-jaeger-exporter" % Versions.trace4catsOtel

  lazy val trace4catsDatadog = "io.janstenpickle"  %% "trace4cats-datadog-http-exporter"  % Versions.trace4catsDatadog
  lazy val trace4catsNewrelic = "io.janstenpickle" %% "trace4cats-newrelic-http-exporter" % Versions.trace4catsNewrelic
  lazy val trace4catsZipkin = "io.janstenpickle"   %% "trace4cats-zipkin-http-exporter"   % Versions.trace4catsZipkin

  lazy val trace4catsZio = "io.janstenpickle" %% "trace4cats-inject-zio" % Versions.trace4catsZio

  lazy val trace4catsNatchez = "io.janstenpickle" %% "trace4cats-natchez" % Versions.trace4catsNatchez

  lazy val trace4catsSttpClient3 = "io.janstenpickle" %% "trace4cats-sttp-client3" % Versions.trace4catsSttp
  lazy val trace4catsTapir = "io.janstenpickle"       %% "trace4cats-sttp-tapir"   % Versions.trace4catsSttp

  lazy val trace4catsHttp4sClient = "io.janstenpickle" %% "trace4cats-http4s-client" % Versions.trace4catsHttp4s
  lazy val trace4catsHttp4sServer = "io.janstenpickle" %% "trace4cats-http4s-server" % Versions.trace4catsHttp4s

  lazy val trace4catsDynamicSamplingHttp4s =
    "io.janstenpickle" %% "trace4cats-dynamic-sampling-http4s" % Versions.trace4catsDynamicSamplingExtras
  lazy val trace4catsDynamicSamplingHttpServer =
    "io.janstenpickle" %% "trace4cats-dynamic-sampling-http-server" % Versions.trace4catsDynamicSamplingExtras

  lazy val trace4catsTailSamplingCache =
    "io.janstenpickle" %% "trace4cats-tail-sampling-cache-store" % Versions.trace4catsTailSamplingExtras
  lazy val trace4catsTailSamplingRedis =
    "io.janstenpickle" %% "trace4cats-tail-sampling-redis-store" % Versions.trace4catsTailSamplingExtras

  lazy val catsEffect = "org.typelevel"                        %% "cats-effect"         % Versions.catsEffect
  lazy val http4sBlazeClient = "org.http4s"                    %% "http4s-blaze-client" % Versions.http4s
  lazy val http4sBlazeServer = "org.http4s"                    %% "http4s-blaze-server" % Versions.http4s
  lazy val sttpClient3Http4s = "com.softwaremill.sttp.client3" %% "http4s-backend"      % Versions.sttpClient3

  lazy val kindProjector = ("org.typelevel" % "kind-projector"     % Versions.kindProjector).cross(CrossVersion.full)
  lazy val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
}
