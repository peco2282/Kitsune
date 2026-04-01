package com.peco2282.kitsune.errorsafe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SafeResultNewFeaturesTest {

    @Test
    fun testCatchSuccess() {
        val result = AppSafe.safe {
            "Success"
        }.catch {
            "Fallback"
        }.apply()

        assertEquals("Success", result)
    }

    @Test
    fun testCatchFailure() {
        val result = AppSafe.safe {
            throw RuntimeException("Error")
            @Suppress("UNREACHABLE_CODE")
            "Success"
        }.catch {
            "Fallback"
        }.apply()

        assertEquals("Fallback", result)
    }

    @Test
    fun testFinallySuccess() {
        var finallyCalled = false
        AppSafe.safe {
            "Success"
        }.finally {
            finallyCalled = true
        }.apply()

        assertTrue(finallyCalled, "Finally should be called on success")
    }

    @Test
    fun testFinallyFailure() {
        var finallyCalled = false
        AppSafe.safe {
            throw RuntimeException("Error")
        }.finally {
            finallyCalled = true
        }.apply()

        assertTrue(finallyCalled, "Finally should be called on failure")
    }

    @Test
    fun testRecover() {
        val result = AppSafe.safe {
            throw RuntimeException("Initial Error")
            @Suppress("UNREACHABLE_CODE")
            "Success"
        }.recover {
            AppSafe.success("Recovered")
        }.apply()

        assertEquals("Recovered", result)
    }

    @Test
    fun testRecoverFailure() {
        val result = AppSafe.safe {
            throw RuntimeException("Initial Error")
            @Suppress("UNREACHABLE_CODE")
            "Success"
        }.recover {
            AppSafe.failure(RuntimeException("Recovery Error"))
        }.catch {
            "Caught after recovery failed"
        }.apply()

        assertEquals("Caught after recovery failed", result)
    }

    @Test
    fun testAppSafeFactories() {
        val success = AppSafe.success("OK").apply()
        assertEquals("OK", success)

        var errorCaught = false
        AppSafe.failure<String>(RuntimeException("Fail"))
            .onErrorListener { errorCaught = true }
            .apply()
        assertTrue(errorCaught)
    }

    @Test
    fun testAppSafeAll() {
        val r1 = AppSafe.success("A")
        val r2 = AppSafe.success("B")
        val result = AppSafe.all(r1, r2).apply()

        assertEquals(listOf("A", "B"), result)
    }

    @Test
    fun testAppSafeAllFailure() {
        val r1 = AppSafe.success("A")
        val r2 = AppSafe.failure<String>(RuntimeException("Fail"))
        
        var errorCaught = false
        AppSafe.all(r1, r2)
            .onErrorListener { errorCaught = true }
            .apply()

        assertTrue(errorCaught)
    }
}
