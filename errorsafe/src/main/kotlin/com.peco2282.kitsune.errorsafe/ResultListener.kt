package com.peco2282.kitsune.errorsafe

interface ResultListener<T> {
  fun onSuccessListener(action: (T) -> Unit): ResultListener<T>
  fun <E : Throwable> onErrorListener(type: Class<E>, action: (E) -> Unit): ResultListener<T>
  fun onErrorListener(action: (AppError) -> Unit): ResultListener<T>

  /**
   * エラーが発生した場合に代替の値を返します。
   */
  fun catch(fallback: (AppError) -> T): ResultListener<T>

  /**
   * エラーが発生した場合に、別の [SafeResult] で処理を継続します。
   */
  fun recover(action: (AppError) -> SafeResult<T>): ResultListener<T>

  /**
   * 成功・失敗に関わらず、処理の最後に実行されます。
   */
  fun finally(action: () -> Unit): ResultListener<T>

  /**
   * 成功した結果を保持する [SafeResult] を返します。
   */
  fun <U> map(transform: (T) -> U): ResultListener<U>

  fun <U> then(action: (T) -> U): ResultListener<U>
  
  fun apply(): T?
}
