# Kitsune

[![English](https://img.shields.io/badge/lang-English-blue.svg)](README.md) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](#)

Kitsuneは、高性能で使いやすいKotlinヘルパーライブラリのコレクションです。日付操作、暗号化、リフレクション、ネットワークなど、Kotlin開発における一般的なタスクを簡素化するための様々なモジュールを提供します。

## モジュール

- **[collection](collection/README.ja.md)**: コレクション、イテラブル、ストリームのユーティリティ。
- **[config](config/README.ja.md)**: 型安全な設定管理。
- **[date](date/README.ja.md)**: 日付と時刻の操作とフォーマット。
- **[encrypt](encrypt/README.ja.md)**: マルチプラットフォーム暗号化 (JVM, Android, Native)。
- **[errorsafe](errorsafe/README.ja.md)**: 関数型エラーハンドリング。
- **[i18n](i18n/README.ja.md)**: 国際化とローカライゼーション。
- **[injector](injector/README.ja.md)**: シンプルな依存性注入。
- **[io](io/README.ja.md)**: DSLベースのファイルとストリーム操作。
- **[logging](logging/README.ja.md)**: JVMとAndroid向けのシンプルなロギング抽象化。
- **[reflect](reflect/README.ja.md)**: リフレクションDSLと最適化されたアクセサ。
- **[resilience](resilience/README.ja.md)**: サーキットブレーカー、リトライ、タイムアウトパターン。
- **[socklet](socklet/README.ja.md)**: シンプルなTCPクライアントとサーバーの実装。
- **[syntactical](syntactical/README.ja.md)**: シンタックスシュガーと中置関数。
- **[task](task/README.ja.md)**: タスクスケジューリングと管理。

## インストール

`build.gradle.kts`にリポジトリと必要なモジュールを追加してください。

```kotlin
repositories {
    maven("https://maven.peco2282.com/repository/maven-releases")
}

dependencies {
    implementation("com.peco2282.kitsune:kitsune-core:1.0.0")
    // 必要に応じて他のモジュールを追加
}
```

## ライセンス

このプロジェクトはApache License, Version 2.0の下でライセンスされています。
