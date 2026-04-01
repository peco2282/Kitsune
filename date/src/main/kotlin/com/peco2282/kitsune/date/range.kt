package com.peco2282.kitsune.date

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class DateRange(
  override val start: LocalDate,
  override val endInclusive: LocalDate,
  private val step: Period = 1.days
) : ClosedRange<LocalDate>, Iterable<LocalDate> {

  override fun iterator(): Iterator<LocalDate> = object : Iterator<LocalDate> {
    private var current = start

    override fun hasNext(): Boolean = current < endInclusive

    override fun next(): LocalDate {
      val next = current
      current = current.plus(step)
      return next
    }
  }

  infix fun step(period: Period): DateRange = DateRange(start, endInclusive, period)
}

operator fun LocalDate.rangeTo(other: LocalDate): DateRange = DateRange(this, other)


class LocalDateTimeRange(
  override val start: LocalDateTime,
  override val endInclusive: LocalDateTime,
  private val step: Duration = 1.daysDuration
) : ClosedRange<LocalDateTime>, Iterable<LocalDateTime> {

  override fun iterator(): Iterator<LocalDateTime> = object : Iterator<LocalDateTime> {
    private var current = start

    override fun hasNext(): Boolean = current <= endInclusive
    override fun next(): LocalDateTime {
      val next = current
      current = current.plus(step)
      return next
    }
  }

  infix fun step(duration: Duration): LocalDateTimeRange =
    LocalDateTimeRange(start, endInclusive, duration)
}

operator fun LocalDateTime.rangeTo(other: LocalDateTime): LocalDateTimeRange =
  LocalDateTimeRange(this, other)
