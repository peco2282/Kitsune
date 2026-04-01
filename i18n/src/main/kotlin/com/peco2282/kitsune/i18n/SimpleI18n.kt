package com.peco2282.kitsune.i18n

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * プロパティファイルを使用せず、メモリ上で翻訳データを保持するシンプルなI18n実装。
 */
class SimpleI18n(
  val config: I18nConfig = I18nConfig(
    File("."),
    "properties"
  ) {
    replacerLeft = "%"
    replacerRight = "%"
  }
) : Translation {
  private val translations = ConcurrentHashMap<String, MutableMap<String, String>>()

  /**
   * 翻訳データを追加します。
   * @param locale ロケール
   * @param key キー
   * @param value 翻訳後の文字列
   */
  fun set(locale: String, key: String, value: String) {
    translations.getOrPut(locale) { ConcurrentHashMap() }[key] = value
  }

  override fun translate(
    locale: String,
    key: String,
    replace: Map<String, String>
  ): String? {
    val localeTranslations = translations[locale] ?: return null
    val value = localeTranslations[key] ?: return null
    return replace.keys.fold(value) { acc, k ->
      acc.replace(config.replaceKey(k), replace[k] ?: "")
    }
  }
}
