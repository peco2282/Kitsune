package com.peco2282.kitsune.socklet

import com.peco2282.kitsune.socklet.client.AdvancedClient
import com.peco2282.kitsune.socklet.client.AutoReconnectClient
import com.peco2282.kitsune.socklet.client.ReconnectPolicy
import com.peco2282.kitsune.socklet.client.SimpleTcpClient
import com.peco2282.kitsune.socklet.server.SimpleMessageServer
import kotlin.test.Test

class C2STest {
  @Test
  fun testEcho() {
    val server = SimpleMessageServer()
    server.onMessage { client, msg ->
      println("Received: $msg")
      client.sendMessage("echo: $msg")
    }
    server.start(9000)

    // クライアント側
    val client = SimpleTcpClient()
    client.connect("localhost", 9000)

    client.sendMessage("Hello Server")
    println(client.readMessage()) // echo: Hello Server

    client.sendMessage("Kotlin is fun")
    println(client.readMessage()) // echo: Kotlin is fun

    client.close()
    server.stop()
  }

//  @Test
  fun testReconnect() {
    // サーバーを最初に起動
    val server = SimpleMessageServer()
    server.onMessage { client, msg ->
      println("Server received: $msg")
      client.sendMessage("echo: $msg")
    }
    server.start(9000)

    // クライアント作成（無限リトライ設定）
    val client = AutoReconnectClient()
    client.setReconnectPolicy(
      ReconnectPolicy(
        maxAttempts = Int.MAX_VALUE, // 永遠にリトライ
        delayMillis = 1000,          // 最初は 1 秒
        backoffFactor = 1.5          // 徐々に待機時間を伸ばす
      )
    )
    client.connect("localhost", 9000)

    // サーバーがある間に通信
    client.sendMessage("first hello")
    println("Client received: ${client.readMessage()}")

    println(">>> サーバーを止めます")
    server.stop()

    // サーバーが落ちている間に送信（このとき再接続が走る）
    repeat(3) {
      try {
        client.sendMessage("msg while down $it")
        println("Client received: ${client.readMessage()}")
      } catch (e: Exception) {
        println("send failed (as expected): ${e.message}")
      }
      Thread.sleep(2000)
    }

    println(">>> サーバーを再起動します")
    val server2 = SimpleMessageServer()
    server2.onMessage { clientConn, msg ->
      println("Server2 received: $msg")
      clientConn.sendMessage("echo2: $msg")
    }
    server2.start(9000)

    // 再接続が成功したあと通信できるはず
    repeat(3) {
      client.sendMessage("msg after restart $it")
      println("Client received: ${client.readMessage()}")
      Thread.sleep(1000)
    }

    client.close()
    server2.stop()
  }

//  @Test
  fun testAdvancedReconnect() {

    val server = SimpleMessageServer()
    server.onMessage { client, msg ->
      println("Server received: $msg")
      client.sendMessage("echo: $msg")
    }
    server.start(9000)

    val client = AdvancedClient()
    client.setReconnectPolicy(ReconnectPolicy(maxAttempts = Int.MAX_VALUE, delayMillis = 1000))

    client.onMessage { msg ->
      println("Server says: $msg")
    }

    client.connect("localhost", 9000)

    client.sendMessage("Hello with callback")
    Thread.sleep(2000)

    client.enableHeartbeat(intervalMillis = 3000, pingMessage = "HEARTBEAT")

    Thread.sleep(15000) // ハートビートでサーバーが応答するのを観察

    client.close()
    server.stop()
  }

}
