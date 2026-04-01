package com.peco2282.kitsune.resilience

data class RetryPolicy(
    val maxAttempts: Int = 3,
    val initialDelayMillis: Long = 1000L,
    val maxDelayMillis: Long = 30000L,
    val backoff: BackoffStrategy = BackoffStrategy.Fixed,
    val jitter: JitterStrategy = JitterStrategy.None,
    val retryOn: (Throwable) -> Boolean = { e ->
        e !is InterruptedException && !isCancellationException(e)
    },
    val onRetry: (RetryEvent) -> Unit = {}
) {
    init {
        require(maxAttempts >= 1) { "maxAttempts must be >= 1" }
        require(initialDelayMillis >= 0) { "initialDelayMillis must be >= 0" }
        require(maxDelayMillis >= initialDelayMillis) { "maxDelayMillis must be >= initialDelayMillis" }
    }

    companion object {
        private fun isCancellationException(e: Throwable): Boolean {
            // Coroutines を依存に入れないため、クラス名で判定する
            val name = e.javaClass.name
            return name == "kotlinx.coroutines.CancellationException" || 
                   name == "java.util.concurrent.CancellationException"
        }
    }
}

data class RetryEvent(
    val attempt: Int, // 1-indexed (the attempt that failed)
    val nextDelayMillis: Long,
    val error: Throwable,
    val startedAtEpochMillis: Long
)
