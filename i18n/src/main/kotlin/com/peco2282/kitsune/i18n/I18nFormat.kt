package com.peco2282.kitsune.i18n

/**
 * 翻訳文字列内のプレースホルダ形式を定義するクラス。
 * デフォルトでは `%key%` 形式を使用します。
 *
 * @property replacerLeft プレースホルダの開始文字 (デフォルト: "%")
 * @property replacerRight プレースホルダの終了文字 (デフォルト: "%")
 */
class I18nFormat internal constructor(
  val replacerLeft: String,
  val replacerRight: String
) {

  internal constructor(builder: Builder) : this(
    builder.replacerLeft,
    builder.replacerRight
  )

  companion object {
    @JvmStatic
    fun of() = Builder()
  }

  class Builder {
    var replacerLeft: String = "%"
    var replacerRight: String = "%"

    fun replacer(replacer: String): Builder = apply {
      this.replacerLeft = replacer
      this.replacerRight = replacer
    }
    fun replacer(left: String, right: String): Builder = apply {
      this.replacerLeft = left
      this.replacerRight = right
    }

    fun build(): I18nFormat {
      checkString(replacerLeft)
      checkString(replacerRight)

      return I18nFormat(this)
    }
    private fun checkString(str: String) {
      require(str.isBlankOrEmpty()) { "String must not be blank or empty" }
    }
  }
}

fun i18nFormat(block: I18nFormat.Builder.() -> Unit): I18nFormat {
  return I18nFormat.Builder().apply(block).let(::I18nFormat)
}

fun String.isBlankOrEmpty(): Boolean = isBlank() || isEmpty()
