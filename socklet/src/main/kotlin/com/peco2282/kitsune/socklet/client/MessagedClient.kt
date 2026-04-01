package com.peco2282.kitsune.socklet.client

interface MessagedClient : MessageClient {
  fun onMessage(handler: (String) -> Unit)
}
