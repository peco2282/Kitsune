# kitsune-socklet

[![English](https://img.shields.io/badge/lang-English-blue.svg)](#) [![日本語](https://img.shields.io/badge/lang-日本語-red.svg)](README.ja.md)

Simple TCP client and server implementation for Kotlin.

## Features

- Lightweight TCP client and server.
- Support for message-based communication.
- Heartbeat and reconnection support.
- Customizable reconnection policies.

## Usage

```kotlin
val server = TcpServer(8080)
server.start()
```
