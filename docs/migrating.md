# Migrating to `0.14.0` and Above

Version `0.14.0` of Trace4Cats introduced a new module and package structure. The motivation for this is to simplify the
the number of imports and dependencies a user must add to their projects.

The best way to migrate is to use [Scala Steward], however there may be some cases where you need to migrate manually.
This guide covers how to perform the migration.

## Scalafix

First add the Scalafix plugin to you `project/plugins.sbt` file:

```scala
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.1")
```

Then run the following command to run the rules.

```bash
sbt "scalafixEnable; scalafix --rules github:trace4cats/trace4cats-scalafix/v0_14?sha=v0.14.0"
```

## Dependencies

To simplify the dependency tree some Trace4Cats libraries have been merged or renamed. As a result, when updating to
`0.14.0` and above may result in the required libary not being found. [Scala Steward] should take care of this for you,
but just in case it doesn't work or you're performing a manual migration you can use the table below to update your
dependencies:

| Old Module                    | Renamed/Consolidated Module     |
|-------------------------------|---------------------------------|
| `trace4cats-core`             | `trace4cats-kernel`             |
| `trace4cats-model`            | `trace4cats-kernel`             |
| `trace4cats-exporter-common`  | `trace4cats-core`               |
| `trace4cats-log-exporter`     | `trace4cats-core`               |
| `trace4cats-inject`           | `trace4cats-core`               |
| `trace4cats-rate-sampling`    | `trace4cats-core`               |
| `trace4cats-dynamic-sampling` | `trace4cats-core`               |
| `trace4cats-filtering`        | `trace4cats-core`               |
| `trace4cats-base`             | `trace4cats-context-utils`      |
| `trace4cats-base-laws`        | `trace4cats-context-utils-laws` |

[Scala Steward]: https://github.com/scala-steward-org/scala-steward
