@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.peco2282.kitsune.date

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period

fun LocalDate.startOfMonth(): LocalDate = this.withDayOfMonth(1)

fun LocalDate.endOfMonth(): LocalDate = this.withDayOfMonth(this.lengthOfMonth())

fun LocalDate.startOfWeek(startDay: DayOfWeek = DayOfWeek.MONDAY): LocalDate {
  var date = this
  while (date.dayOfWeek != startDay) {
    date = date.minusDays(1)
  }
  return date
}

fun LocalDate.endOfWeek(endDay: DayOfWeek = DayOfWeek.SUNDAY): LocalDate {
  var date = this
  while (date.dayOfWeek != endDay) {
    date = date.plusDays(1)
  }
  return date
}

fun LocalDate.isWeekend(): Boolean = this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY

fun LocalDate.isWeekday(): Boolean = !this.isWeekend()

val Int.days: Period get() = Period.ofDays(this)
val Int.weeks: Period get() = Period.ofWeeks(this)
val Int.months: Period get() = Period.ofMonths(this)
val Int.years: Period get() = Period.ofYears(this)

operator fun LocalDate.plus(period: Period): LocalDate = this.plus(period)
operator fun LocalDate.minus(period: Period): LocalDate = this.minus(period)
