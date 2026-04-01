package com.peco2282.kitsune.errorsafe

object AppSafe {
  /**
   * ブロックを実行し、その結果を [SafeResult] として返します。
   * 実行は [SafeResult.apply] が呼ばれるまで遅延されます。
   */
  fun <T> safe(block: () -> T): SafeResult<T> {
    return SafeResult(block)
  }

  /**
   * 成功した結果を生成します。
   */
  fun <T> success(value: T): SafeResult<T> {
    return SafeResult.success(value)
  }

  /**
   * 失敗した結果を生成します。
   */
  fun <T> failure(error: Throwable): SafeResult<T> {
    return SafeResult.failure(error)
  }

  /**
   * 複数の [SafeResult] を結合し、すべて成功した場合はそのリストを返します。
   * 一つでも失敗した場合は、最初の失敗を返します。
   */
  fun <T> all(vararg results: SafeResult<out T>): SafeResult<List<T>> {
    return safe {
      results.map { it.apply() ?: throw IllegalStateException("Result applied but returned null") }
    }
  }
}
