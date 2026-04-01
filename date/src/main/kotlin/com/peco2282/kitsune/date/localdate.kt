@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.peco2282.kitsune.date

import java.time.Duration
import java.time.LocalDateTime

val Int.seconds: Duration get() = Duration.ofSeconds(this.toLong())
val Int.minutes: Duration get() = Duration.ofMinutes(this.toLong())
val Int.hours: Duration get() = Duration.ofHours(this.toLong())
val Int.daysDuration: Duration get() = Duration.ofDays(this.toLong())

operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = this.plus(duration)
operator fun LocalDateTime.minus(duration: Duration): LocalDateTime = this.minus(duration)
