# kitsune-io

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

DSL-based file and stream operations for Kotlin.

## Features

- DSL for file reading and writing.
- Stream manipulation utilities.
- Simplified resource management.

## Usage

```kotlin
file("example.txt").read { 
    println(it)
}
```
