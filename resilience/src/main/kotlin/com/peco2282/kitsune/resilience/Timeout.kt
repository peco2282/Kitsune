package com.peco2282.kitsune.resilience

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

data class TimeoutPolicy(val timeoutMillis: Long)

/**
 * Executes the given [block] with a timeout.
 * 
 * NOTE: The cancellation of the block is best-effort (via thread interruption).
 * It does not guarantee immediate or certain termination of the block.
 */
fun <T> withTimeout(
    policy: TimeoutPolicy,
    block: () -> T
): T {
    val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable).apply { isDaemon = true }
    }
    try {
        val future = executor.submit<T> { block() }
        return try {
            future.get(policy.timeoutMillis, TimeUnit.MILLISECONDS)
        } catch (e: TimeoutException) {
            future.cancel(true)
            throw e
        } catch (e: Exception) {
            throw e.cause ?: e
        }
    } finally {
        executor.shutdownNow()
    }
}
