package com.peco2282.kitsune.socklet.server

interface TcpServer: AutoCloseable {
  fun start(port: Int)
  fun stop()
  fun isRunning(): Boolean
}
