package com.peco2282.kitsune.i18n

/**
 * 翻訳機能を提供するインターフェース。
 */
interface Translation {
  /**
   * 指定されたロケールとキーに基づいて翻訳を行います。
   *
   * @param locale ロケール名 (例: "ja", "en")
   * @param key 翻訳対象のキー
   * @param replace 置換するパラメータのマップ。キーはプレースホルダ名、値は置換後の文字列
   * @return 翻訳された文字列。見つからない場合は null
   */
  fun translate(locale: String, key: String, replace: Map<String, String>): String?
}
