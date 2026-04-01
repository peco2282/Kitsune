# Kitsune

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Kitsune is a collection of high-performance, easy-to-use Kotlin helper libraries. It provides various modules to simplify common tasks in Kotlin development, including date manipulation, encryption, reflection, networking, and more.

## Modules

- **[collection](collection/README.md)**: Utilities for collections, iterables, and streams.
- **[config](config/README.md)**: Type-safe configuration management.
- **[date](date/README.md)**: Date and time manipulation and formatting.
- **[encrypt](encrypt/README.md)**: Multi-platform encryption (JVM, Android, Native).
- **[errorsafe](errorsafe/README.md)**: Functional error handling.
- **[i18n](i18n/README.md)**: Internationalization and localization.
- **[injector](injector/README.md)**: Simple dependency injection.
- **[io](io/README.md)**: DSL-based file and stream operations.
- **[logging](logging/README.md)**: Simple logging abstraction for JVM and Android.
- **[reflect](reflect/README.md)**: Reflection DSL and performance-optimized accessors.
- **[resilience](resilience/README.md)**: Circuit breaker, retry, and timeout patterns.
- **[socklet](socklet/README.md)**: Simple TCP client and server implementation.
- **[syntactical](syntactical/README.md)**: Syntactic sugar and infix functions.
- **[task](task/README.md)**: Task scheduling and management.

## Installation

Add the repository and the desired modules to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://maven.peco2282.com/repository/maven-releases")
}

dependencies {
    implementation("com.peco2282.kitsune:kitsune-core:1.0.0")
    // Add other modules as needed
}
```

## License

This project is licensed under the Apache License, Version 2.0.
