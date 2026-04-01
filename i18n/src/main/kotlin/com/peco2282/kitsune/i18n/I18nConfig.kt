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
  val format: I18nFormat
) {
  /**
   * キーをプレースホルダ形式に変換します。
   */
  fun replaceKey(key: String) = format.replacerLeft + key + format.replacerRight
}
