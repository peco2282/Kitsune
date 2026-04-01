package com.peco2282.kitsune.resilience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

class ResilienceTest {

    class MockDelayProvider : DelayProvider {
        var totalSleepTime = 0L
        var sleepCount = 0
        override fun sleep(millis: Long) {
            totalSleepTime += millis
            sleepCount++
        }
    }

    class MockClock(var now: Long = 0) : Clock {
        override fun nowMillis(): Long = now
    }

    @Test
    fun `test retry max attempts`() {
        val attempts = AtomicInteger(0)
        val policy = RetryPolicy(maxAttempts = 3)
        
        assertThrows<RuntimeException> {
            retry(policy) {
                attempts.incrementAndGet()
                throw RuntimeException("fail")
            }
        }
        
        assertEquals(3, attempts.get())
    }

    @Test
    fun `test retry retryOn suppression`() {
        val attempts = AtomicInteger(0)
        val policy = RetryPolicy(
            maxAttempts = 3,
            retryOn = { e -> e is IllegalArgumentException }
        )
        
        assertThrows<IllegalStateException> {
            retry(policy) {
                attempts.incrementAndGet()
                throw IllegalStateException("critical")
            }
        }
        
        assertEquals(1, attempts.get())
    }

    @Test
    fun `test retry backoff and jitter clamping`() {
        val delayProvider = MockDelayProvider()
        val onRetryCalls = mutableListOf<Long>()
        val policy = RetryPolicy(
            maxAttempts = 3,
            initialDelayMillis = 100,
            maxDelayMillis = 500,
            backoff = BackoffStrategy.Linear(stepMillis = 1000), // 100, 1100
            onRetry = { onRetryCalls.add(it.nextDelayMillis) }
        )
        
        assertThrows<RuntimeException> {
            retry(policy, delayProvider = delayProvider) {
                throw RuntimeException("fail")
            }
        }
        
        assertEquals(2, delayProvider.sleepCount)
        assertEquals(2, onRetryCalls.size)
        
        // linear backoff would be 100, then 100 + 1000 = 1100.
        // clamped to maxDelayMillis = 500.
        assertTrue(onRetryCalls[0] <= 500)
        assertTrue(onRetryCalls[1] <= 500)
        assertEquals(500, onRetryCalls[1])
    }

    @Test
    fun `test withTimeout`() {
        val policy = TimeoutPolicy(timeoutMillis = 100)
        
        assertThrows<TimeoutException> {
            withTimeout(policy) {
                Thread.sleep(500)
                "done"
            }
        }
        
        val result = withTimeout(policy) {
            "success"
        }
        assertEquals("success", result)
    }

    @Test
    fun `test circuit breaker`() {
        val clock = MockClock()
        val policy = CircuitBreakerPolicy(
            failureThreshold = 2,
            openDurationMillis = 1000,
            halfOpenMaxAttempts = 1
        )
        val cb = CircuitBreaker(policy, clock)

        // Initial state: CLOSED
        assertEquals(CircuitState.CLOSED, cb.getState())

        // Failure 1
        assertThrows<RuntimeException> { cb.execute { throw RuntimeException() } }
        assertEquals(CircuitState.CLOSED, cb.getState())

        // Failure 2 -> OPEN
        assertThrows<RuntimeException> { cb.execute { throw RuntimeException() } }
        assertEquals(CircuitState.OPEN, cb.getState())

        // Still OPEN
        assertThrows<CircuitOpenException> { cb.execute { "call" } }

        // Advance clock -> HALF_OPEN
        clock.now = 1001
        // First call in HALF_OPEN should be allowed
        val result = cb.execute { "success" }
        assertEquals("success", result)
        // Success in HALF_OPEN (threshold 1) -> CLOSED
        assertEquals(CircuitState.CLOSED, cb.getState())
    }
}
