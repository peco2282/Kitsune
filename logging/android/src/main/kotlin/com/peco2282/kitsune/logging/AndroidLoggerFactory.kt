package com.peco2282.kitsune.logging

import android.util.Log as AndroidLog

class AndroidLogger(override val tag: String) : Logger {
    private val finalTag = if (tag.length > 23) {
        tag.substring(0, 23)
    } else {
        tag
    }

    override fun isEnabled(level: LogLevel): Boolean {
        val priority = toPriority(level)
        return AndroidLog.isLoggable(finalTag, priority) || level.ordinal >= LogLevel.INFO.ordinal
    }

    override fun log(level: LogLevel, throwable: Throwable?, fields: Map<String, Any?>, message: () -> String) {
        if (!isEnabled(level)) return

        val priority = toPriority(level)
        val msg = message()
        val fieldsString = if (fields.isNotEmpty()) {
            fields.toSortedMap().entries.joinToString(" ") { "${it.key}=${it.value ?: "null"}" }.let { " $it" }
        } else ""

        val fullMsg = "$msg$fieldsString"

        if (throwable != null) {
            AndroidLog.println(priority, finalTag, fullMsg + "\n" + AndroidLog.getStackTraceString(throwable))
        } else {
            AndroidLog.println(priority, finalTag, fullMsg)
        }
    }

    override fun log(
        level: LogLevel,
        throwable: Throwable?,
        fields: Map<String, Any?>,
        message: String
    ) {
        if (!isEnabled(level)) return

        val priority = toPriority(level)
        val fieldsString = if (fields.isNotEmpty()) {
            fields.toSortedMap().entries.joinToString(" ") { "${it.key}=${it.value ?: "null"}" }.let { " $it" }
        } else ""

        val fullMsg = "$message$fieldsString"

        if (throwable != null) {
            AndroidLog.println(priority, finalTag, fullMsg + "\n" + AndroidLog.getStackTraceString(throwable))
        } else {
            AndroidLog.println(priority, finalTag, fullMsg)
        }
    }

    private fun toPriority(level: LogLevel): Int {
        return when (level) {
            LogLevel.TRACE -> AndroidLog.VERBOSE
            LogLevel.DEBUG -> AndroidLog.DEBUG
            LogLevel.INFO -> AndroidLog.INFO
            LogLevel.WARN -> AndroidLog.WARN
            LogLevel.ERROR -> AndroidLog.ERROR
        }
    }
}

class AndroidLoggerFactory : LoggerFactory {
    override operator fun get(tag: String): Logger = AndroidLogger(tag)
}
