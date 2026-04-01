package com.peco2282.kitsune.resilience

sealed interface BackoffStrategy {
    fun nextDelayMillis(attempt: Int, initialDelayMillis: Long): Long

    object Fixed : BackoffStrategy {
        override fun nextDelayMillis(attempt: Int, initialDelayMillis: Long): Long = initialDelayMillis
    }

    data class Linear(val stepMillis: Long) : BackoffStrategy {
        override fun nextDelayMillis(attempt: Int, initialDelayMillis: Long): Long {
            return initialDelayMillis + (attempt - 1) * stepMillis
        }
    }

    data class Exponential(val multiplier: Double) : BackoffStrategy {
        override fun nextDelayMillis(attempt: Int, initialDelayMillis: Long): Long {
            return (initialDelayMillis * Math.pow(multiplier, (attempt - 1).toDouble())).toLong()
        }
    }
}

sealed interface JitterStrategy {
    fun applyJitter(delayMillis: Long): Long

    object None : JitterStrategy {
        override fun applyJitter(delayMillis: Long): Long = delayMillis
    }

    object Full : JitterStrategy {
        override fun applyJitter(delayMillis: Long): Long {
            return if (delayMillis <= 0) 0 else (0..delayMillis).random()
        }
    }

    object Equal : JitterStrategy {
        override fun applyJitter(delayMillis: Long): Long {
            return if (delayMillis <= 0) 0 else (delayMillis / 2) + (0..(delayMillis / 2)).random()
        }
    }
}
