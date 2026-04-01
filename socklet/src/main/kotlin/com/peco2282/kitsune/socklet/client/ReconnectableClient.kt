package com.peco2282.kitsune.socklet.client

interface ReconnectableClient:MessageClient {
  fun setReconnectPolicy(policy: ReconnectPolicy)
}
