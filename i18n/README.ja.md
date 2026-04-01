# kitsune-i18n

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けの国際化とローカライゼーションのサポート。

## 特徴

- 複数言語の翻訳管理。
- 動的な言語切り替え。
- フォーマット済み文字列のサポート。

## 使い方

```kotlin
val i18n = I18nManager()
val message = i18n.translate("hello.world")
```
