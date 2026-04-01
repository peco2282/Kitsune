package com.peco2282.kitsune.resilience

interface Clock {
    fun nowMillis(): Long
}

interface DelayProvider {
    fun sleep(millis: Long)
}

object Defaults {
    var clock: Clock = object : Clock {
        override fun nowMillis(): Long = System.currentTimeMillis()
    }

    var delay: DelayProvider = object : DelayProvider {
        override fun sleep(millis: Long) {
            if (millis > 0) Thread.sleep(millis)
        }
    }
}
