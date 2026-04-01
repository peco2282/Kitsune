package com.peco2282.kitsune.date

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RangeTest {
  @Test
  fun rangeTo() {
    val start = LocalDate.of(2025, 9, 1)
    val end = LocalDate.of(2025, 9, 7)

    val range = start..end

    assertTrue(LocalDate.of(2025, 9, 3) in range)
    assertFalse(LocalDate.of(2025, 9, 10) in range)

    println("Iterate 1 day step:")
    for (date in range) {
      println(date)
    }

    println("Iterate 2 days step:")
    for (date in range step 2.days) {
      println(date)
    }
  }

}
