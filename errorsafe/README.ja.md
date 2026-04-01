# kitsune-errorsafe

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けの関数型エラーハンドリング。

## 特徴

- エラー伝搬のための `SafeResult`。
- 安全な操作のための DSL。
- エラーハンドリングの非同期サポート。

## 使い方

```kotlin
val result = safe {
    // リスクのある操作
}
result.onSuccess { value -> println(value) }
      .onFailure { error -> println(error) }
```
