package com.peco2282.kitsune.socklet.client

data class ReconnectPolicy(
  val maxAttempts: Int = 5,
  val delayMillis: Long = 1000L,
  val backoffFactor: Double = 2.0
) {
  companion object {
    val DEFAULT = ReconnectPolicy()
  }
}
