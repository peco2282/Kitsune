# kitsune-injector

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

軽量な依存性注入ライブラリ。

## 特徴

- サービスベースのDI。
- アノテーションを使用しない注入。
- シングルトンおよびトランジェントサービスのサポート。

## 使い方

```kotlin
val injector = Injector()
injector.register(UserService::class, UserServiceImpl())
val service = injector.inject(UserService::class)
```
