# kitsune-reflect

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

KotlinおよびJava向けのリフレクションDSLと最適化されたアクセサ。

## 特徴

- 簡単なリフレクションアクセスのためのDSL。
- フィールド、メソッド、コンストラクタ向けの最適化されたアクセサ。
- KotlinとJavaの両方のリフレクションをサポート。
- チェインアクセサのサポート。

## 使い方

```kotlin
val value = target.reflect {
    field("privateField").get<String>()
}
```
