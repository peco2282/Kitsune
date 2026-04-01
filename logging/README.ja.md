# kitsune-logging

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

JVMとAndroid向けのシンプルなロギング抽象化。

## モジュール

- **api**: コアロガーインターフェース。
- **jvm**: JVM向けのコンソールロガー実装。
- **android**: Android向けのLogcatロガー実装。

## 特徴

- プラットフォーム間での統一されたロギングAPI。
- 差し替え可能なロガーファクトリ。
- メッセージの遅延評価。

## 使い方

```kotlin
val logger = Logger.getLogger("MyTag")
logger.info { "Message with ${dynamicValue}" }
```
