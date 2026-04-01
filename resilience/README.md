# kitsune-resilience

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Resilience patterns for Kotlin: Circuit Breaker, Retry, and Timeout.

## Features

- **Circuit Breaker**: Prevent system failure by stopping requests to failing services.
- **Retry**: Automatically retry failed operations with configurable strategies.
- **Timeout**: Enforce time limits on long-running operations.

## Usage

```kotlin
val retry = Retry(RetryConfig(maxAttempts = 3))
retry.execute {
    // risky operation
}
```
