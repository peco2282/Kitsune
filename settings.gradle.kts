import org.gradle.kotlin.dsl.maven

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

fun submodule(path: String, vararg type: String) {
  include(type.map { "$path-$it" })

  type.forEach {
    project(":$path-$it").projectDir = file("$path/$it")
  }
}

rootProject.name = "Kitsune"
include(":date")

submodule("encrypt", "api", "jvm", "android")

include("syntactical")
include("reflect")
include("task")
include("i18n")
include("socklet")
include("collection")

include("injector")

include("errorsafe")
include("resilience")
submodule("logging", "api", "jvm", "android")

include("io")
include("config")