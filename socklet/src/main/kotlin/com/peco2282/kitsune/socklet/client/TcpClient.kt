package com.peco2282.kitsune.socklet.client

interface TcpClient : AutoCloseable {
  fun connect(host: String, port: Int)
  fun send(data: ByteArray)
  fun isConnected(): Boolean
  fun receive(): ByteArray?
}
