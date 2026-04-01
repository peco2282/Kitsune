package com.peco2282.kitsune.resilience

fun <T> retry(
    policy: RetryPolicy,
    clock: Clock = Defaults.clock,
    delayProvider: DelayProvider = Defaults.delay,
    block: () -> T
): T {
    var attempt = 1
    val startedAt = clock.nowMillis()

    while (true) {
        try {
            return block()
        } catch (e: Throwable) {
            if (!policy.retryOn(e) || attempt >= policy.maxAttempts) {
                throw e
            }

            val rawDelay = policy.backoff.nextDelayMillis(attempt, policy.initialDelayMillis)
            val jitteredDelay = policy.jitter.applyJitter(rawDelay)
            val nextDelay = jitteredDelay.coerceIn(0, policy.maxDelayMillis)

            policy.onRetry(RetryEvent(attempt, nextDelay, e, startedAt))

            delayProvider.sleep(nextDelay)
            attempt++
        }
    }
}

fun <T> retryCatching(
    policy: RetryPolicy,
    clock: Clock = Defaults.clock,
    delayProvider: DelayProvider = Defaults.delay,
    block: () -> T
): Result<T> {
    return runCatching {
        retry(policy, clock, delayProvider, block)
    }
}
