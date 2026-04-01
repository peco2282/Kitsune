package com.peco2282.kitsune.socklet.client

interface MessageClient: TcpClient {
  fun sendMessage(message: String)
  fun readMessage(): String?
}
