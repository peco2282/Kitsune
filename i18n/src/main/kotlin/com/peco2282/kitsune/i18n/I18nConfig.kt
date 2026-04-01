package com.peco2282.kitsune.i18n

import java.io.File

/**
 * I18nの設定情報を保持するクラス。
 *
 * @property resourceDir 翻訳ファイルが格納されているディレクトリ
 * @property extention 翻訳ファイルの拡張子 (デフォルト: "properties")
 * @property format プレースホルダの形式
 */
class I18nConfig(
  val resourceDir: File,
  val extention: String = "properties",
  format: I18nFormat.Builder.() -> Unit
) {
  private val _format: I18nFormat by lazy { I18nFormat.of().apply(format).build() }

  /**
   * キーをプレースホルダ形式に変換します。
   */
  fun replaceKey(key: String) = _format.replacerLeft + key + _format.replacerRight
}
