# kitsune-task

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Task scheduling and management for Kotlin.

## Features

- Flexible task scheduling (one-time, periodic).
- Task handler for managing task lifecycle.
- Error handling during task execution.
- DSL for task building.

## Usage

```kotlin
val scheduler = Scheduler()
scheduler.schedule {
    // task logic
}
```
