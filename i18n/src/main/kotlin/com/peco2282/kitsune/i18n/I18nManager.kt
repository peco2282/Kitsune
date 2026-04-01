package com.peco2282.kitsune.i18n

import java.util.concurrent.atomic.AtomicReference

/**
 * I18nインスタンスをグローバルに管理するオブジェクト。
 */
object I18nManager {
    private val provider = AtomicReference<Translation>(SimpleI18n())

    /**
     * 使用する Translation 実装を設定します。
     */
    fun set(translation: Translation) {
        provider.set(translation)
    }

    /**
     * 現在設定されている Translation 実装を返します。
     */
    fun get(): Translation = provider.get()

    /**
     * 指定されたロケールとキーに基づいて翻訳を行います。
     */
    fun translate(locale: String, key: String, replace: Map<String, String> = emptyMap()): String? =
        get().translate(locale, key, replace)

    /**
     * 翻訳を行い、見つからない場合はデフォルト値を返します。
     * デフォルト値内のプレースホルダも置換されます。
     */
    fun translateOrDefault(locale: String, key: String, default: String, replace: Map<String, String> = emptyMap()): String {
        val translated = translate(locale, key, replace)
        if (translated != null) return translated
        
        val p = get()
        val config = when (p) {
            is I18n -> p.config
            is SimpleI18n -> p.config
            else -> null
        }
        
        return if (config != null) {
            replace.keys.fold(default) { acc, k ->
                acc.replace(config.replaceKey(k), replace[k] ?: "")
            }
        } else {
            default
        }
    }
}
