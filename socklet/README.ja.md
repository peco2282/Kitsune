# kitsune-socklet

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kotlin向けのシンプルなTCPクライアントとサーバーの実装。

## 特徴

- 軽量なTCPクライアントとサーバー。
- メッセージベースの通信のサポート。
- ハートビートと再接続のサポート。
- カスタマイズ可能な再接続ポリシー。

## 使い方

```kotlin
val server = TcpServer(8080)
server.start()
```
