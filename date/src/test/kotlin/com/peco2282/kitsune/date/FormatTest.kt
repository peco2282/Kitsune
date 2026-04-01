package com.peco2282.kitsune.date

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class FormatTest {
  @Test
  fun timeIsoFormat() {
    val date = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
    assertEquals(date.isoFormat(), "2023-01-01T00:00:00")
  }

  @Test
  fun dateIsoFormat() {
    val date = LocalDate.of(2023, 1, 1)
    assertEquals(date.isoFormat(), "2023-01-01")
  }

  @Test
  fun timeJapaneseFormat() {
    val date = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
    assertEquals(date.japaneseFormat(), "2023年01月01日 00時00分00秒")
  }

  @Test
  fun dateJapaneseFormat() {
    val date = LocalDate.of(2023, 1, 1)
    assertEquals(date.japaneseFormat(), "2023年01月01日")
  }

  @Test
  fun timeFormat() {
    val date = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
    assertEquals(date.format("yyyy-MM-dd HH:mm:ss"), "2023-01-01 00:00:00")
  }

  @Test
  fun dateFormat() {
    val date = LocalDate.of(2023, 1, 1)
    assertEquals(date.format("yyyy-MM-dd"), "2023-01-01")
  }

}
