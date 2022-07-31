package trace4cats.example

import cats.effect.{IO, IOApp}
import trace4cats._
import trace4cats.attributes.{EnvironmentAttributes, HostAttributes, SystemPropertyAttributes}
import trace4cats.model.AttributeValue.{LongValue, StringValue}

object TraceProcessAttributes extends IOApp.Simple {
  override def run: IO[Unit] =
    TraceProcessBuilder("some-service")
      .withAttributes(EnvironmentAttributes.includeKeys[IO](Set("HOME")))
      .withAttributes(SystemPropertyAttributes.filterKeys[IO](_.contains("xyz")))
      .withAttributes(HostAttributes[IO])
      .withAttributes(IO(Map("side" -> StringValue("effect"))))
      .withAttributes("pure" -> LongValue(1))
      .build
      .flatMap(attrs => IO(println(attrs)))
}
