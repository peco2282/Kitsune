package com.peco2282.kitsune.socklet.server

interface ClientConnection: AutoCloseable {
  fun sendMessage(message: String)
  fun readMessage(): String?
}
