package com.peco2282.kitsune.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.util.*

class ConfigTest {

  @Test
  fun testConfigDelegation() {
    val props = Properties().apply {
      setProperty("server.port", "8080")
      setProperty("server.host", "localhost")
      setProperty("app.debug", "true")
      setProperty("app.timeout", "5000")
    }

    val config = object : Config(props) {
      val port by int("server.port") { 3000 }
      val host by string("server.host") { "127.0.0.1" }
      val debug by boolean("app.debug") { false }
      val timeout by long("app.timeout") { 1000L }
      val missing by string("missing.key") { "default" }
    }

    assertEquals(8080, config.port)
    assertEquals("localhost", config.host)
    assertTrue(config.debug)
    assertEquals(5000L, config.timeout)
    assertEquals("default", config.missing)
  }

  @Test
  fun testLoadConfig() {
    val configStr = """
            server.port=9090
            server.host=example.com
        """.trimIndent()

    val config = Config()
    config.load(ByteArrayInputStream(configStr.toByteArray()))

    val port by config.int("server.port", 8080)
    val host by config.string("server.host", "localhost")

    assertEquals(9090, port)
    assertEquals("example.com", host)
  }
}
