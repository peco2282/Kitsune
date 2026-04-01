# kitsune-errorsafe

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Functional error handling for Kotlin.

## Features

- `SafeResult` for error propagation.
- DSL for safe operations.
- Async support for error handling.

## Usage

```kotlin
val result = safe {
    // some risky operation
}
result.onSuccess { value -> println(value) }
      .onFailure { error -> println(error) }
```
