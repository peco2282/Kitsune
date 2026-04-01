package com.peco2282.kitsune.socklet.client

import java.io.*
import java.net.Socket

import com.peco2282.kitsune.resilience.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class SimpleTcpClient : MessageClient {
  private var socket: Socket? = null
  private var input: BufferedReader? = null
  private var output: BufferedWriter? = null

  override fun connect(host: String, port: Int) {
    socket = Socket(host, port)
    input = socket!!.getInputStream().bufferedReader()
    output = socket!!.getOutputStream().bufferedWriter()
  }

  // 接続状態を確認して送信
  override fun send(data: ByteArray) {
    checkConnected()
    socket!!.getOutputStream().write(data)
    socket!!.getOutputStream().flush()
  }

  override fun isConnected(): Boolean {
    return socket != null && socket!!.isConnected && !socket!!.isClosed
  }

  override fun receive(): ByteArray? {
    checkConnected()
    val buffer = ByteArray(1024)
    val read = socket!!.getInputStream().read(buffer)
    return if (read > 0) buffer.copyOf(read) else null
  }

  override fun sendMessage(message: String) {
    checkConnected()
    output!!.write(message)
    output!!.newLine()
    output!!.flush()
  }

  override fun readMessage(): String? {
    checkConnected()
    return input!!.readLine()
  }

  private fun checkConnected() {
    if (!isConnected()) {
      throw IOException("Socket is not connected or has been closed")
    }
  }

  override fun close() {
    input?.close()
    output?.close()
    socket?.close()
  }
}

class AutoReconnectClient : ReconnectableClient {
  private var socket: Socket? = null
  private var input: BufferedReader? = null
  private var output: BufferedWriter? = null

  private var host: String = ""
  private var port: Int = 0
  private var policy = ReconnectPolicy.DEFAULT
  private var connected = false

  override fun connect(host: String, port: Int) {
    this.host = host
    this.port = port
    attemptConnect()
  }

  private fun attemptConnect() {
    val retryPolicy = RetryPolicy(
        maxAttempts = policy.maxAttempts,
        initialDelayMillis = policy.delayMillis,
        backoff = BackoffStrategy.Exponential(policy.backoffFactor),
        onRetry = { event ->
            println("Connect failed (attempt ${event.attempt}): ${event.error.message}")
            println("Retrying in ${event.nextDelayMillis} ms...")
        }
    )

    try {
        retry(retryPolicy) {
            socket = Socket(host, port)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            output = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
            connected = true
            println("Connected to $host:$port")
        }
    } catch (e: Exception) {
        throw IOException("Failed to connect after ${policy.maxAttempts} attempts", e)
    }
  }

  override fun setReconnectPolicy(policy: ReconnectPolicy) {
    this.policy = policy
  }

  override fun send(data: ByteArray) {
    try {
      socket?.getOutputStream()?.apply {
        write(data)
        flush()
      }
    } catch (e: IOException) {
      reconnect()
      send(data)
    }
  }

  override fun isConnected(): Boolean = connected

  override fun receive(): ByteArray? {
    return try {
      val buffer = ByteArray(1024)
      val read = socket?.getInputStream()?.read(buffer) ?: return null
      if (read > 0) buffer.copyOf(read) else null
    } catch (e: IOException) {
      reconnect()
      null
    }
  }

  override fun sendMessage(message: String) {
    try {
      output?.apply {
        write(message)
        newLine()
        flush()
      }
    } catch (e: IOException) {
      reconnect()
      sendMessage(message)
    }
  }

  override fun readMessage(): String? {
    return try {
      input?.readLine()
    } catch (e: IOException) {
      reconnect()
      null
    }
  }

  private fun reconnect() {
    println("Lost connection, attempting reconnect...")
    connected = false
    attemptConnect()
  }

  override fun close() {
    connected = false
    input?.close()
    output?.close()
    socket?.close()
  }
}

class AdvancedClient : MessagedClient, ReconnectClient, HeartbeatClient {
  private var socket: Socket? = null
  private var input: BufferedReader? = null
  private var output: BufferedWriter? = null

  private var host: String = ""
  private var port: Int = 0
  private var policy = ReconnectPolicy()
  private val connected = AtomicBoolean(false)

  private var messageHandler: ((String) -> Unit)? = null
  private val executor = Executors.newSingleThreadExecutor()

  private var heartbeatThread: Thread? = null
  private val heartbeatRunning = AtomicBoolean(false)
  private var heartbeatInterval: Long = 5000
  private var heartbeatMessage: String = "PING"

  override fun connect(host: String, port: Int) {
    this.host = host
    this.port = port
    attemptConnect()
    startReceiver()
  }

  private fun attemptConnect() {
    var attempt = 0
    var delay = policy.delayMillis

    while (!connected.get() && (policy.maxAttempts == Int.MAX_VALUE || attempt < policy.maxAttempts)) {
      try {
        socket = Socket(host, port)
        input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        output = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
        connected.set(true)
        println("Connected to $host:$port")
        return
      } catch (e: Exception) {
        attempt++
        println("Connect failed (attempt $attempt): ${e.message}")
        Thread.sleep(delay)
        delay = (delay * policy.backoffFactor).toLong()
      }
    }

    if (!connected.get()) throw IOException("Failed to connect after $attempt attempts")
  }

  private fun startReceiver() {
    executor.execute {
      while (true) {
        try {
          val msg = input?.readLine()
          if (msg != null) {
            messageHandler?.invoke(msg)
          } else {
            reconnect()
          }
        } catch (e: IOException) {
          reconnect()
        }
      }
    }
  }

  private fun reconnect() {
    if (!connected.get()) return
    println("Lost connection, attempting reconnect...")
    connected.set(false)
    closeResources()
    attemptConnect()
  }

  override fun setReconnectPolicy(policy: ReconnectPolicy) {
    this.policy = policy
  }

  override fun send(data: ByteArray) {
    try {
      socket?.getOutputStream()?.write(data)
      socket?.getOutputStream()?.flush()
    } catch (e: IOException) {
      reconnect()
      send(data)
    }
  }

  override fun isConnected(): Boolean {
    return connected.get()
  }

  override fun receive(): ByteArray? {
    return try {
      val buffer = ByteArray(1024)
      val read = socket?.getInputStream()?.read(buffer) ?: return null
      if (read > 0) buffer.copyOf(read) else null
    } catch (e: IOException) {
      reconnect()
      null
    }
  }

  override fun sendMessage(message: String) {
    try {
      output?.write(message)
      output?.newLine()
      output?.flush()
    } catch (e: IOException) {
      reconnect()
      sendMessage(message)
    }
  }

  override fun readMessage(): String? {
    return try {
      input?.readLine()
    } catch (e: IOException) {
      reconnect()
      null
    }
  }

  override fun onMessage(handler: (String) -> Unit) {
    this.messageHandler = handler
  }

  override fun enableHeartbeat(intervalMillis: Long, pingMessage: String) {
    heartbeatInterval = intervalMillis
    heartbeatMessage = pingMessage
    heartbeatRunning.set(true)

    heartbeatThread = thread(start = true, isDaemon = true) {
      while (heartbeatRunning.get()) {
        try {
          sendMessage(heartbeatMessage)
          Thread.sleep(heartbeatInterval)
        } catch (_: Exception) {
        }
      }
    }
  }

  override fun disableHeartbeat() {
    heartbeatRunning.set(false)
    heartbeatThread?.interrupt()
  }

  private fun closeResources() {
    try {
      input?.close()
    } catch (_: Exception) {
    }
    try {
      output?.close()
    } catch (_: Exception) {
    }
    try {
      socket?.close()
    } catch (_: Exception) {
    }
  }

  override fun close() {
    connected.set(false)
    disableHeartbeat()
    closeResources()
    executor.shutdown()
  }
}
