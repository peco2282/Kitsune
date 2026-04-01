package com.peco2282.kitsune.socklet.client

interface HeartbeatClient: MessageClient {
  fun enableHeartbeat(intervalMillis: Long = 5000, pingMessage: String = "PING")
  fun disableHeartbeat()
}
