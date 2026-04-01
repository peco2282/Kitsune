package com.peco2282.kitsune.config

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import kotlin.reflect.KProperty

/**
 * 設定管理の基底クラス
 */
open class Config(private val properties: Properties = Properties()) {
  fun load(inputStream: InputStream) {
    properties.load(inputStream)
  }

  fun load(path: String) {
    properties.load(Config::class.java.getResourceAsStream(path))
  }

  fun load(file: File) {
    properties.load(FileInputStream(file))
  }

  // 委譲プロパティの定義
  fun string(key: String, default: String = "") = ConfigDelegate(key, { default }) { it }
  fun string(key: String, default: () -> String = { "" }) = ConfigDelegate(key, default) { it }
  fun int(key: String, default: Int = 0) = ConfigDelegate(key, { default }) { it.toInt() }
  fun int(key: String, default: () -> Int = { 0 }) = ConfigDelegate(key, default) { it.toInt() }
  fun boolean(key: String, default: Boolean = false) = ConfigDelegate(key, { default }) { it.toBoolean() }
  fun boolean(key: String, default: () -> Boolean = { false }) = ConfigDelegate(key, default) { it.toBoolean() }
  fun long(key: String, default: Long = 0L) = ConfigDelegate(key, { default }) { it.toLong() }
  fun long(key: String, default: () -> Long = { 0L }) = ConfigDelegate(key, default) { it.toLong() }

  inner class ConfigDelegate<T>(
    private val key: String,
    private val default: () -> T,
    private val mapper: (String) -> T
  ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
      val value = properties.getProperty(key)
      return if (value != null) mapper(value) else default()
    }
  }
}
