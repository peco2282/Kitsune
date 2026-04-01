# kitsune-encrypt

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

JVM、Android、Native向けのマルチプラットフォーム暗号化ライブラリ。

## モジュール

- **api**: コア暗号化インターフェースとJNIサポート。
- **jvm**: JVM固有の実装（例：AES）。
- **android**: Android固有の実装。

## 特徴

- AES暗号化と復号化。
- 安全な鍵管理。
- JNIベースのネイティブ暗号サポート。
