package com.peco2282.kitsune.socklet.client

interface ReconnectClient: MessageClient {
  fun setReconnectPolicy(policy: ReconnectPolicy)
}
