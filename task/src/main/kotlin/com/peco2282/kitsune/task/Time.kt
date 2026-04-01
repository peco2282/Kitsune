package com.peco2282.kitsune.task

@JvmInline
value class Time(val milliseconds: Long) {
  companion object {
    val IMMEDIATELY = Time(0)
    val MILLIS = Time(milliseconds = 0)
    val SECOND: Time = 1.seconds
    val MINUTE: Time = 1.minutes
    val HOUR: Time = 1.hours
  }
}

val Int.milliseconds
  get() = Time(this.toLong())
val Long.milliseconds
  get() = Time(this)

val Double.seconds
  get() = Time((this * 1000).toLong())
val Float.seconds
  get() = Time((this * 1000).toLong())
val Int.seconds
  get() = Time(this.toLong() * 1000)
val Long.seconds
  get() = Time(this * 1000)

val Double.minutes
  get() = Time((this * 60 * 1000).toLong())
val Float.minutes
  get() = Time((this * 60 * 1000).toLong())
val Int.minutes
  get() = Time(this.toLong() * 60 * 1000)
val Long.minutes
  get() = Time(this * 60 * 1000)

val Double.hours
  get() = Time((this * 60 * 60 * 1000).toLong())
val Float.hours
  get() = Time((this * 60 * 60 * 1000).toLong())
val Int.hours
  get() = Time(this.toLong() * 60 * 60 * 1000)
val Long.hours
  get() = Time(this * 60 * 60 * 1000)
