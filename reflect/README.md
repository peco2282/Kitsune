# kitsune-reflect

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Reflection DSL and performance-optimized accessors for Kotlin and Java.

## Features

- DSL for easy reflection access.
- Performance-optimized accessors for fields, methods, and constructors.
- Support for both Kotlin and Java reflection.
- Chained accessor support.

## Usage

```kotlin
val value = target.reflect {
    field("privateField").get<String>()
}
```
