# kitsune-io

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けのDSLベースのファイルとストリーム操作。

## 特徴

- ファイルの読み書きのためのDSL。
- ストリーム操作ユーティリティ。
- 簡素化されたリソース管理。

## 使い方

```kotlin
file("example.txt").read { 
    println(it)
}
```
