package com.peco2282.kitsune.injector

/**
 * 例外を投げる可能性がある実行可能なサービスを表す関数型インターフェース。
 */
@FunctionalInterface
fun interface ThrowableService {
  /**
   * サービスを実行します。
   *
   * @throws Throwable 実行中に発生した例外
   */
  @Throws(Throwable::class)
  fun run()
}
