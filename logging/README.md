# kitsune-logging

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Simple logging abstraction for JVM and Android.

## Modules

- **api**: Core logger interfaces.
- **jvm**: Console logger implementation for JVM.
- **android**: Android Logcat logger implementation.

## Features

- Unified logging API across platforms.
- Pluggable logger factories.
- Lazy message evaluation.

## Usage

```kotlin
val logger = Logger.getLogger("MyTag")
logger.info { "Message with ${dynamicValue}" }
```
