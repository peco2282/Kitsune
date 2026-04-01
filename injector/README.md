# kitsune-injector

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Lightweight dependency injection library.

## Features

- Service-based DI.
- Annotation-less injection.
- Support for singleton and transient services.

## Usage

```kotlin
val injector = Injector()
injector.register(UserService::class, UserServiceImpl())
val service = injector.inject(UserService::class)
```
