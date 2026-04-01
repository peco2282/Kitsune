package com.peco2282.kitsune.i18n

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.*

class I18nTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun testSimpleI18n() {
        val simpleI18n = SimpleI18n()
        simpleI18n.set("ja", "hello", "こんにちは")
        simpleI18n.set("ja", "greet", "こんにちは、%name%さん")
        simpleI18n.set("en", "hello", "Hello")

        assertEquals("こんにちは", simpleI18n.translate("ja", "hello", emptyMap()))
        assertEquals("Hello", simpleI18n.translate("en", "hello", emptyMap()))
        assertEquals("こんにちは、太郎さん", simpleI18n.translate("ja", "greet", mapOf("name" to "太郎")))
        assertNull(simpleI18n.translate("fr", "hello", emptyMap()))
        assertNull(simpleI18n.translate("ja", "unknown", emptyMap()))
    }

    @Test
    fun testI18nWithProperties() {
        val jaProps = File(tempDir, "ja.properties")
        jaProps.writeText("hello=こんにちは\ngreet=こんにちは、%name%さん\n")
        
        val enProps = File(tempDir, "en.properties")
        enProps.writeText("hello=Hello\n")

        val config = I18nConfig(tempDir, "properties") { replacer("%") }
      val i18n = I18n(config, listOf("ja", "en"))

        val jaHello = i18n.translate("ja", "hello", emptyMap())
        println("[DEBUG_LOG] ja hello: $jaHello")
        assertEquals("こんにちは", jaHello)
        
        val enHello = i18n.translate("en", "hello", emptyMap())
        println("[DEBUG_LOG] en hello: $enHello")
        assertEquals("Hello", enHello)
        
        val jaGreet = i18n.translate("ja", "greet", mapOf("name" to "太郎"))
        println("[DEBUG_LOG] ja greet: $jaGreet")
        assertEquals("こんにちは、太郎さん", jaGreet)
    }

    @Test
    fun testI18nManager() {
        val simpleI18n = SimpleI18n()
        simpleI18n.set("ja", "hello", "こんにちは")
        I18nManager.set(simpleI18n)

        assertEquals("こんにちは", I18nManager.translate("ja", "hello"))
        assertEquals("Default", I18nManager.translateOrDefault("ja", "unknown", "Default"))
        assertEquals("Hello, John", I18nManager.translateOrDefault("ja", "unknown", "Hello, %name%", mapOf("name" to "John")))
    }
}
