# kitsune-resilience

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けのレジリエンスパターン：サーキットブレーカー、リトライ、タイムアウト。

## 特徴

- **サーキットブレーカー**: 失敗しているサービスへのリクエストを停止し、システム全体の障害を防ぎます。
- **リトライ**: 失敗した操作を設定可能な戦略で自動的に再試行します。
- **タイムアウト**: 長時間実行される操作に時間制限を設けます。

## 使い方

```kotlin
val retry = Retry(RetryConfig(maxAttempts = 3))
retry.execute {
    // リスクのある操作
}
```
