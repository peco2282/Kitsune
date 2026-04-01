package com.peco2282.kitsune.errorsafe

class SafeResult<T>(
  private val block: (() -> T)? = null,
  private var value: T? = null,
  private var error: Throwable? = null,
) : ResultListener<T> {

  private val successListeners = mutableListOf<(T) -> Unit>()
  private val errorListeners = mutableListOf<(AppError) -> Unit>()
  private val specificErrorListeners = mutableListOf<Pair<Class<out Throwable>, (Throwable) -> Unit>>()
  private val finallyListeners = mutableListOf<() -> Unit>()
  private val transformations = mutableListOf<(SafeResult<T>) -> Unit>()
  
  private var parent: SafeResult<*>? = null
  private var isApplied = false

  override fun onSuccessListener(action: (T) -> Unit): SafeResult<T> {
    successListeners.add(action)
    return this
  }

  override fun <E : Throwable> onErrorListener(type: Class<E>, action: (E) -> Unit): SafeResult<T> {
    @Suppress("UNCHECKED_CAST")
    specificErrorListeners.add(type to (action as (Throwable) -> Unit))
    return this
  }

  override fun onErrorListener(action: (AppError) -> Unit): SafeResult<T> {
    errorListeners.add(action)
    return this
  }

  override fun catch(fallback: (AppError) -> T): SafeResult<T> {
    val nextResult = SafeResult<T>()
    nextResult.parent = this
    transformations.add { result ->
      val currentError = result.error
      if (currentError != null) {
        try {
          val fallbackValue = fallback(AppError.wrap(currentError))
          nextResult.fulfill(fallbackValue, null)
        } catch (e: Throwable) {
          nextResult.fulfill(null, e)
        }
      } else {
        nextResult.fulfill(result.value, null)
      }
    }
    return nextResult
  }

  override fun recover(action: (AppError) -> SafeResult<T>): SafeResult<T> {
    val nextResult = SafeResult<T>()
    nextResult.parent = this
    
    transformations.add { result ->
      val currentError = result.error
      if (currentError != null) {
        try {
          val recoveryResult = action(AppError.wrap(currentError))
          recoveryResult.onSuccessListener { nextResult.fulfill(it, null) }
          recoveryResult.onErrorListener { nextResult.fulfill(null, it) }
          recoveryResult.apply()
        } catch (e: Throwable) {
          nextResult.fulfill(null, e)
        }
      } else {
        nextResult.fulfill(result.value, null)
      }
    }
    return nextResult
  }

  override fun finally(action: () -> Unit): SafeResult<T> {
    finallyListeners.add(action)
    return this
  }
  
  override fun <U> map(transform: (T) -> U): SafeResult<U> {
    return then(transform)
  }

  override fun <U> then(action: (T) -> U): SafeResult<U> {
    val nextResult = SafeResult<U>()
    nextResult.parent = this
    
    transformations.add { result ->
      val currentError = result.error
      val currentValue = result.value
      if (currentError != null) {
        nextResult.fulfill(null, currentError)
      } else if (currentValue != null) {
        try {
          @Suppress("UNCHECKED_CAST")
          val nextValue = action(currentValue as T)
          nextResult.fulfill(nextValue, null)
        } catch (e: Throwable) {
          nextResult.fulfill(null, e)
        }
      }
    }
    return nextResult
  }

  override fun apply(): T? {
    val p = parent
    if (p != null) {
      p.apply()
    } else {
      if (block != null && !isApplied && value == null && error == null) {
        try {
          value = block()
        } catch (e: Throwable) {
          error = e
        }
      }
      execute()
    }
    return value
  }

  internal fun fulfill(v: T?, e: Throwable?) {
    this.value = v
    this.error = e
    execute()
  }

  private fun execute() {
    if (isApplied) return
    val currentError = error
    val currentValue = value
    
    // まだ値もエラーもない場合は実行を待機（thenチェイン用）
    if (currentError == null && currentValue == null) return
    
    isApplied = true

    if (currentError != null) {
      val wrapped = AppError.wrap(currentError)
      errorListeners.forEach { it(wrapped) }
      specificErrorListeners.forEach { (type, action) ->
        val found = wrapped.findCause(type)
        if (found != null) {
          action(found)
        }
      }
    } else if (currentValue != null) {
      successListeners.forEach { it(currentValue) }
    }
    
    transformations.forEach { it(this) }
    finallyListeners.forEach { it() }
  }

  companion object {
    fun <T> success(value: T): SafeResult<T> = SafeResult(null, value, null)
    fun <T> failure(error: Throwable): SafeResult<T> = SafeResult(null, null, error)
  }
}
