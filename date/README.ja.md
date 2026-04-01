# kitsune-date

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けの日付と時刻の操作とフォーマット。

## 特徴

- シンプルな日付フォーマット。
- 相対時間計算（例：「3日前」）。
- 日付範囲ユーティリティ。
- LocalDateの拡張。

## 使い方

```kotlin
val now = LocalDate.now()
val formatted = now.format("yyyy/MM/dd")
```
