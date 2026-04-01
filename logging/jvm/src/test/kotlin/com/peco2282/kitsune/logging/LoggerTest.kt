package com.peco2282.kitsune.logging

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LoggerTest {

    @Test
    fun testLazyEvaluation() {
        var evaluated = false
        val logger = ConsoleLogger("Test", LogLevel.INFO)
        
        // TRACE is disabled by default INFO minLevel
        logger.trace {
            evaluated = true
            "Trace message"
        }
        assertFalse(evaluated, "Message should not be evaluated when level is disabled")

        logger.info {
            evaluated = true
            "Info message"
        }
        assertTrue(evaluated, "Message should be evaluated when level is enabled")
    }

    @Test
    fun testFieldsSorting() {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        val originalOut = System.out
        System.setOut(ps)

        try {
            val logger = ConsoleLogger("Test", LogLevel.INFO)
            val fields = mapOf("z" to "last", "a" to "first", "m" to 123, "n" to null)
            
            logger.info(fields = fields) { "Message {z} {a} {m} {n}" }
            
            val output = baos.toString()
            assertTrue(output.contains("Message last first 123 null"), "Fields should be sorted by key: $output")
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun testThrowable() {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        val originalErr = System.err
        System.setErr(ps)

        try {
            val logger = ConsoleLogger("Test", LogLevel.WARN)
            val exception = RuntimeException("Test exception")
            
            logger.error(exception) { "Error message" }
            
            val output = baos.toString()
            assertTrue(output.contains("ERROR Test - Error message"), "Log should contain level and message")
            assertTrue(output.contains("java.lang.RuntimeException: Test exception"), "Log should contain exception info")
        } finally {
            System.setErr(originalErr)
        }
    }
}
