package com.peco2282.kitsune.resilience

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

data class CircuitBreakerPolicy(
    val failureThreshold: Int = 5,
    val openDurationMillis: Long = 60000L,
    val halfOpenMaxAttempts: Int = 3
)

class CircuitOpenException(message: String) : Exception(message)

enum class CircuitState {
    CLOSED, OPEN, HALF_OPEN
}

class CircuitBreaker(
    val policy: CircuitBreakerPolicy,
    private val clock: Clock = Defaults.clock
) {
    private val state = AtomicReference(CircuitState.CLOSED)
    private val failureCount = AtomicInteger(0)
    private val lastFailureTime = AtomicLong(0)
    private val halfOpenSuccessCount = AtomicInteger(0)

    fun <T> execute(block: () -> T): T {
        updateState()

        val currentState = state.get()
        if (currentState == CircuitState.OPEN) {
            throw CircuitOpenException("Circuit is OPEN until ${lastFailureTime.get() + policy.openDurationMillis}")
        }

        return try {
            val result = block()
            onSuccess()
            result
        } catch (e: Throwable) {
            onFailure()
            throw e
        }
    }

    private fun updateState() {
        val currentState = state.get()
        if (currentState == CircuitState.OPEN) {
            if (clock.nowMillis() - lastFailureTime.get() >= policy.openDurationMillis) {
                state.compareAndSet(CircuitState.OPEN, CircuitState.HALF_OPEN)
                halfOpenSuccessCount.set(0)
            }
        }
    }

    private fun onSuccess() {
        when (state.get()) {
            CircuitState.HALF_OPEN -> {
                if (halfOpenSuccessCount.incrementAndGet() >= policy.halfOpenMaxAttempts) {
                    reset()
                }
            }
            CircuitState.CLOSED -> {
                failureCount.set(0)
            }
            else -> {}
        }
    }

    private fun onFailure() {
        lastFailureTime.set(clock.nowMillis())
        when (state.get()) {
            CircuitState.CLOSED -> {
                if (failureCount.incrementAndGet() >= policy.failureThreshold) {
                    state.set(CircuitState.OPEN)
                }
            }
            CircuitState.HALF_OPEN, CircuitState.OPEN -> {
                state.set(CircuitState.OPEN)
            }
        }
    }

    private fun reset() {
        state.set(CircuitState.CLOSED)
        failureCount.set(0)
        halfOpenSuccessCount.set(0)
    }

    fun getState(): CircuitState = state.get()
}
