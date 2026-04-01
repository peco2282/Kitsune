package com.peco2282.kitsune.date

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDate.tomorrow(): LocalDate = this.plusDays(1)
fun LocalDate.yesterday(): LocalDate = this.minusDays(1)
fun LocalDate.nextWeek(): LocalDate = this.plusWeeks(1)
fun LocalDate.lastWeek(): LocalDate = this.minusWeeks(1)
fun LocalDate.nextMonth(): LocalDate = this.plusMonths(1)
fun LocalDate.lastMonth(): LocalDate = this.minusMonths(1)
fun LocalDate.nextYear(): LocalDate = this.plusYears(1)
fun LocalDate.lastYear(): LocalDate = this.minusYears(1)

fun LocalDateTime.tomorrow(): LocalDateTime = this.plusDays(1)
fun LocalDateTime.yesterday(): LocalDateTime = this.minusDays(1)
fun LocalDateTime.nextWeek(): LocalDateTime = this.plusWeeks(1)
fun LocalDateTime.lastWeek(): LocalDateTime = this.minusWeeks(1)
fun LocalDateTime.nextMonth(): LocalDateTime = this.plusMonths(1)
fun LocalDateTime.lastMonth(): LocalDateTime = this.minusMonths(1)
fun LocalDateTime.nextYear(): LocalDateTime = this.plusYears(1)
fun LocalDateTime.lastYear(): LocalDateTime = this.minusYears(1)

fun Long.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime =
  Instant.ofEpochMilli(this).atZone(zone).toLocalDateTime()

fun Long.toLocalDate(zone: ZoneId = ZoneId.systemDefault()): LocalDate =
  Instant.ofEpochMilli(this).atZone(zone).toLocalDate()

fun Long.nanosToLocalDateTime(referenceMillis: Long = System.currentTimeMillis(), zone: ZoneId = ZoneId.systemDefault()): LocalDateTime {
  val nanosPerMilli = 1_000_000L
  val millis = referenceMillis + this / nanosPerMilli
  return Instant.ofEpochMilli(millis).atZone(zone).toLocalDateTime()
}
