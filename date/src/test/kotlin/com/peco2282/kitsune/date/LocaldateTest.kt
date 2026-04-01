package com.peco2282.kitsune.date

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class LocaldateTest {
  @Test
  fun getSeconds() {
    assertEquals(Duration.ofSeconds(30), 30.seconds)
    assertEquals(Duration.ofSeconds(90), 90.seconds)
  }

  @Test
  fun getMinutes() {
    assertEquals(Duration.ofMinutes(1), 1.minutes)
    assertEquals(Duration.ofMinutes(45), 45.minutes)
  }

  @Test
  fun getHours() {
    assertEquals(Duration.ofHours(1), 1.hours)
    assertEquals(Duration.ofHours(12), 12.hours)
  }

  @Test
  fun getDaysDuration() {
    assertEquals(Duration.ofDays(1), 1.daysDuration)
    assertEquals(Duration.ofDays(7), 7.daysDuration)
  }

  @Test
  fun plus() {
    val base = LocalDateTime.of(2025, 9, 1, 12, 0, 0)

    val plus30min = base + 30.minutes
    assertEquals(LocalDateTime.of(2025, 9, 1, 12, 30, 0), plus30min)

    val plus2h = base + 2.hours
    assertEquals(LocalDateTime.of(2025, 9, 1, 14, 0, 0), plus2h)
  }

  @Test
  fun minus() {
    val base = LocalDateTime.of(2025, 9, 1, 12, 0, 0)
    val minus45s = base - 45.seconds
    assertEquals(LocalDateTime.of(2025, 9, 1, 11, 59, 15), minus45s)

    val minus1day = base - 1.daysDuration
    assertEquals(LocalDateTime.of(2025, 8, 31, 12, 0, 0), minus1day)
  }

}
