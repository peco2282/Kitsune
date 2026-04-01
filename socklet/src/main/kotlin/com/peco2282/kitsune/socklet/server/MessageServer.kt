package com.peco2282.kitsune.socklet.server

interface MessageServer: TcpServer {
  fun onMessage(handler: (client: ClientConnection, message: String) -> Unit)
}
