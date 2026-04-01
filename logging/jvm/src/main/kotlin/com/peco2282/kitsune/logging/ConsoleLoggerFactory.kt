package com.peco2282.kitsune.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConsoleLogger(
  override val tag: String,
  private val minLevel: LogLevel
) : Logger {
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

  override fun isEnabled(level: LogLevel): Boolean {
    return level.ordinal >= minLevel.ordinal
  }

  override fun log(level: LogLevel, throwable: Throwable?, fields: Map<String, Any?>, message: () -> String) {
    if (!isEnabled(level)) return

    val out = if (level == LogLevel.WARN || level == LogLevel.ERROR) System.err else System.out
    val timestamp = LocalDateTime.now().format(formatter)
    val msg = message()

    val fieldsString = if (fields.isNotEmpty()) {
      fields.toSortedMap().entries.joinToString(" ") { "${it.key}=${it.value ?: "null"}" }.let { " $it" }
    } else ""

    val line = "$timestamp $level $tag - $msg$fieldsString"
    out.println(line)

    throwable?.printStackTrace(out)
  }

  override fun log(
    level: LogLevel,
    throwable: Throwable?,
    fields: Map<String, Any?>,
    message: String
  ) {
    if (!isEnabled(level)) return

    val out = if (level == LogLevel.WARN || level == LogLevel.ERROR) System.err else System.out
    val timestamp = LocalDateTime.now().format(formatter)

    val fieldsString = if (fields.isNotEmpty()) {
      fields.toSortedMap().entries.joinToString(" ") { "${it.key}=${it.value ?: "null"}" }.let { " $it" }
    } else ""

    val line = "$timestamp $level $tag - $message$fieldsString"
    out.println(line)

    throwable?.printStackTrace(out)
  }
}

class ConsoleLoggerFactory(private val minLevel: LogLevel = LogLevel.INFO) : LoggerFactory {
  override operator fun get(tag: String): Logger = ConsoleLogger(tag, minLevel)
}
