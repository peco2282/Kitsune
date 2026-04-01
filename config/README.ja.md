# kitsune-config

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

プロパティ委譲を使用した型安全な設定管理。

## 特徴

- `Int`, `String`, `Boolean`, `Long` などの委譲プロパティ。
- デフォルト値のサポート。
- `java.util.Properties` からの設定読み込み。

## 使い方

```kotlin
class AppConfig(props: Properties) : Config(props) {
    val port by int("server.port") { 8080 }
    val host by string("server.host") { "localhost" }
}
```
