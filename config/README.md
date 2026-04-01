# kitsune-config

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Type-safe configuration management using property delegation.

## Features

- Delegate properties for `Int`, `String`, `Boolean`, `Long`, etc.
- Support for default values.
- Loads configuration from `java.util.Properties`.

## Usage

```kotlin
class AppConfig(props: Properties) : Config(props) {
    val port by int("server.port") { 8080 }
    val host by string("server.host") { "localhost" }
}
```
