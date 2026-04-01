package com.peco2282.kitsune.date

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateTest {
  val date: LocalDate = LocalDate.of(2023, 1, 1)

  @Test
  fun startOfMonth() {
    assertEquals(date.startOfMonth(), LocalDate.of(2023, 1, 1))
  }

  @Test
  fun endOfMonth() {
    assertEquals(date.endOfMonth(), LocalDate.of(2023, 1, 31))
  }

  @Test
  fun startOfWeek() {
    assertEquals(date.startOfWeek(), LocalDate.of(2022, 12, 26))
  }

  @Test
  fun endOfWeek() {
    assertEquals(date.endOfWeek(), LocalDate.of(2023, 1, 1))
  }

  @Test
  fun isWeekend() {
    assertEquals(date.isWeekend(), true)
  }

  @Test
  fun isWeekday() {
    assertEquals(date.isWeekday(), false)
  }

  @Test
  fun getDays() {
    val today = LocalDate.of(2025, 9, 1)
    val result = today + 3.days
    assertEquals(LocalDate.of(2025, 9, 4), result)
  }

  @Test
  fun getWeeks() {
    val today = LocalDate.of(2025, 9, 1)
    val result = today - 2.weeks
    assertEquals(LocalDate.of(2025, 8, 18), result)
  }

  @Test
  fun getMonths() {
    val date = LocalDate.of(2025, 1, 31)
    val result = date + 1.months
    assertEquals(LocalDate.of(2025, 2, 28), result)
  }

  @Test
  fun getYears() {
    val date = LocalDate.of(2024, 2, 29)
    val result = date + 1.years
    assertEquals(LocalDate.of(2025, 2, 28), result)
  }

}
