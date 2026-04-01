package com.peco2282.kitsune.errorsafe

/**
 * アプリケーションの基本エラークラス。
 * すべての例外はこのクラスにラップされるか、このクラスを継承します。
 */
abstract class AppError(
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause) {

    /** エラーコード */
    abstract val code: String

    override fun toString(): String = "[$code] $message${cause?.let { " (Cause: ${it::class.simpleName}: ${it.message})" } ?: ""}"

    /** 原因不明のエラー */
    class Unknown(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_UNKNOWN"
    }

    /** 実行時に発生したエラー */
    class Execution(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_EXECUTION"
    }

    /** バリデーションエラー */
    class Validation(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_VALIDATION"
    }

    /** ネットワーク関連のエラー */
    class Network(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_NETWORK"
    }

    /** データベース関連のエラー */
    class Database(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_DATABASE"
    }

    /** 認証・認可関連のエラー */
    class Auth(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_AUTH"
    }

    /** 入出力関連のエラー */
    class IO(message: String, cause: Throwable? = null) : AppError(message, cause) {
        override val code: String = "ERR_IO"
    }

    companion object {
        /**
         * 任意の [Throwable] を [AppError] にラップします。
         */
        fun wrap(throwable: Throwable): AppError = when (throwable) {
            is AppError -> throwable
            else -> Execution(throwable.message ?: "An unexpected error occurred", throwable)
        }
    }

    /**
     * 指定された型の例外が原因に含まれているか判定します。
     */
    fun <T : Throwable> isCausedBy(type: Class<T>): Boolean {
        return findCause(type) != null
    }

    /**
     * 原因の中から指定された型の例外を探します。
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Throwable> findCause(type: Class<T>): T? {
        var current: Throwable? = this
        while (current != null) {
            if (type.isInstance(current)) return current as T
            current = current.cause
        }
        return null
    }

    fun asResult(): SafeResult<Nothing> = SafeResult.failure(this)
}
