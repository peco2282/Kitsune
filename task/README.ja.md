# kitsune-task

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けのタスクスケジューリングと管理。

## 特徴

- 柔軟なタスクスケジューリング（単発、定期実行）。
- タスクのライフサイクルを管理するためのタスクハンドラ。
- タスク実行中のエラーハンドリング。
- タスク構築のためのDSL。

## 使い方

```kotlin
val scheduler = Scheduler()
scheduler.schedule {
    // タスクロジック
}
```
