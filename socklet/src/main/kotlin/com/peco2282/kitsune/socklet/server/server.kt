package com.peco2282.kitsune.socklet.server
import com.peco2282.kitsune.logging.logger
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.Executors

class SimpleMessageServer : MessageServer {
  private val logger = logger("SimpleMessageServer")
  private var serverSocket: ServerSocket? = null
  @Volatile
  private var running = false
  private var handler: ((ClientConnection, String) -> Unit)? = null
  private val pool = Executors.newCachedThreadPool()

  override fun start(port: Int) {
    running = true
    serverSocket = ServerSocket(port)

    pool.execute {
      logger.info { "Server started on port $port" }
      try {
        while (running) {
          val client: Socket = try {
            serverSocket?.accept() ?: break
          } catch (e: SocketException) {
            if (!running) break
            else throw e
          }

          if (!pool.isShutdown) {
            pool.execute { handleClient(client) }
          } else {
            client.close()
          }
        }
      } finally {
        logger.info { "Server stopped" }
        pool.shutdown()
      }
    }
  }

  private fun handleClient(client: Socket) {
    client.use { socket ->
      socket.getInputStream().bufferedReader().use { reader ->
        socket.getOutputStream().bufferedWriter().use { writer ->
          var line: String?
          while (reader.readLine().also { line = it } != null) {
            logger.debug { "Server received: $line" }
            this.handler?.invoke(SimpleClientConnection(socket), line!!)
            writer.newLine()
            writer.flush()
          }
        }
      }
    }
  }

  override fun onMessage(handler: (ClientConnection, String) -> Unit) {
    this.handler = handler
  }

  override fun stop() {
    running = false
    try {
      Socket("localhost", serverSocket?.localPort ?: return).close()
    } catch (_: Exception) {
    }
    serverSocket?.close()
    pool.shutdown()
  }

  override fun isRunning(): Boolean {
    return running
  }

  override fun close() = stop()
}

class SimpleClientConnection(private val socket: Socket) : ClientConnection {
  private val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
  private val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

  override fun sendMessage(message: String) {
    writer.write(message)
    writer.newLine()
    writer.flush()
  }

  override fun readMessage(): String? = reader.readLine()

  override fun close() {
    reader.close()
    writer.close()
    socket.close()
  }
}
