package com.peco2282.kitsune.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val ISO_DATE = "yyyy-MM-dd"
const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
const val JAPANESE_DATE = "yyyy年MM月dd日"
const val JAPANESE_DATE_TIME = "yyyy年MM月dd日 HH時mm分ss秒"

fun LocalDateTime.isoFormat(): String = this.format(ISO_DATE_TIME)

fun LocalDate.isoFormat(): String = this.format(ISO_DATE)

fun LocalDateTime.japaneseFormat(): String = this.format(JAPANESE_DATE_TIME)

fun LocalDate.japaneseFormat(): String = this.format(JAPANESE_DATE)

fun LocalDateTime.format(pattern: String): String =
  this.format(DateTimeFormatter.ofPattern(pattern))

fun LocalDate.format(pattern: String): String =
  this.format(DateTimeFormatter.ofPattern(pattern))
