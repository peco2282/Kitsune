package com.peco2282.kitsune.logging

enum class LogLevel {
  TRACE, DEBUG, INFO, WARN, ERROR
}

@Suppress("unused")
interface Logger {
  val tag: String
  fun isEnabled(level: LogLevel): Boolean
  fun log(level: LogLevel, throwable: Throwable? = null, fields: Map<String, Any?> = emptyMap(), message: () -> String)
  fun log(level: LogLevel, throwable: Throwable? = null, fields: Map<String, Any?> = emptyMap(), message: String)

  fun trace(fields: Map<String, Any?> = emptyMap(), message: () -> String) =
    log(LogLevel.TRACE, fields = fields, message = message)

  fun debug(fields: Map<String, Any?> = emptyMap(), message: () -> String) =
    log(LogLevel.DEBUG, fields = fields, message = message)

  fun info(fields: Map<String, Any?> = emptyMap(), message: () -> String) =
    log(LogLevel.INFO, fields = fields, message = message)

  fun warn(fields: Map<String, Any?> = emptyMap(), message: () -> String) =
    log(LogLevel.WARN, fields = fields, message = message)

  fun error(throwable: Throwable? = null, fields: Map<String, Any?> = emptyMap(), message: () -> String) =
    log(LogLevel.ERROR, throwable = throwable, fields = fields, message = message)

  fun trace(fields: Map<String, Any?> = emptyMap(), message: String) =
    log(LogLevel.TRACE, fields = fields, message = message)

  fun debug(fields: Map<String, Any?> = emptyMap(), message: String) =
    log(LogLevel.DEBUG, fields = fields, message = message)

  fun info(fields: Map<String, Any?> = emptyMap(), message: String) =
    log(LogLevel.INFO, fields = fields, message = message)

  fun warn(fields: Map<String, Any?> = emptyMap(), message: String) =
    log(LogLevel.WARN, fields = fields, message = message)

  fun error(throwable: Throwable? = null, fields: Map<String, Any?> = emptyMap(), message: String) =
    log(LogLevel.ERROR, throwable = throwable, fields = fields, message = message)
}

interface LoggerFactory {
  operator fun get(tag: String): Logger
}

object Log {
  @Volatile
  var factory: LoggerFactory = NoopLoggerFactory

  operator fun get(tag: String): Logger = factory[tag]
}

object NoopLogger : Logger {
  override val tag: String = "Noop"
  override fun isEnabled(level: LogLevel): Boolean = false
  override fun log(level: LogLevel, throwable: Throwable?, fields: Map<String, Any?>, message: () -> String) {
    // do nothing
  }

  override fun log(
    level: LogLevel,
    throwable: Throwable?,
    fields: Map<String, Any?>,
    message: String
  ) {
    // do nothing
  }
}

object NoopLoggerFactory : LoggerFactory {
  override fun get(tag: String): Logger = NoopLogger
}

fun logger(tag: String): Logger = Log[tag]
