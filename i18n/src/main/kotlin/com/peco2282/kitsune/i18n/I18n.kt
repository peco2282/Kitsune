package com.peco2282.kitsune.i18n

import com.peco2282.kitsune.logging.logger
import java.io.File
import java.util.*

/**
 * プロパティファイルを使用した翻訳の実装。
 *
 * @property config 設定情報
 * @param locales ロードするロケールのリスト
 */
class I18n(
  val config: I18nConfig,
  locales: Collection<String> = emptySet()
): Translation {

  private val logger = logger("I18n")
  private val translations = loadTranslations(locales)

  private fun loadTranslations(locales: Collection<String>): Map<String, LocatedProperties> {
    val map = mutableMapOf<String, LocatedProperties>()
    for (locale in locales.toSet()) {
      if (locale.isEmpty() || locale.isBlank()) continue
      val localeFile = File(config.resourceDir, "$locale.${config.extention}")
      if (!localeFile.exists()) {
        logger.warn { "Translation file not found: ${localeFile.absolutePath}" }
        continue
      }

      val properties = Properties()
      localeFile.reader(Charsets.UTF_8).use { properties.load(it) }.let { map[locale] = LocatedProperties(config, properties) }
    }

    return map
  }

  override fun translate(locale: String, key: String, replace: Map<String, String>): String? {
    val props = translations[locale]
    if (props == null) {
      logger.warn { "Translation locale not found: $locale" }
      return null
    }
    return props [key, replace]
  }

  /**
   * 翻訳を行い、見つからない場合はデフォルト値を返します。
   *
   * @param locale ロケール名
   * @param key 翻訳対象のキー
   * @param default デフォルト値
   * @param replace 置換するパラメータのマップ
   * @return 翻訳された文字列、またはデフォルト値
   */
  fun translateOrDefault(locale: String, key: String, default: String, replace: Map<String, String> = emptyMap()): String {
    val props = translations[locale]
    if (props == null) {
      logger.warn { "Translation locale not found: $locale" }
      return replace.keys.fold(default) { acc, k -> acc.replace(config.replaceKey(k), replace[k] ?: "") }
    }
    return props[key, replace] ?: replace.keys.fold(default) { acc, k -> acc.replace(config.replaceKey(k), replace[k] ?: "") }
  }

  /**
   * 特定のロケールに関連付けられたプロパティを保持する内部クラス。
   */
  class LocatedProperties internal constructor(val config: I18nConfig, val props: Properties) {
    /**
     * キーに対応する値を取得し、プレースホルダを置換します。
     */
    operator fun get(key: String, replace: Map<String, String> = emptyMap()): String? {
      val value = props.getProperty(key)
      if (value == null) {
        logger("I18n").warn { "Translation key not found: $key" }
        return null
      }
      return replace.keys.fold(value) { acc, k -> acc.replace(config.replaceKey(k), replace[k] ?: "") }
    }
  }
}
