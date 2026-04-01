package com.peco2282.kitsune.injector

/**
 * 実行可能なサービスを表す関数型インターフェース。
 */
@FunctionalInterface
fun interface Service {
  /**
   * サービスを実行します。
   */
  fun run()
}
