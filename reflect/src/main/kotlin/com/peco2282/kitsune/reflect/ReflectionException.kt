package com.peco2282.kitsune.reflect

class ReflectionException(override val message: String?, override val cause: Throwable?) :
  RuntimeException(message, cause) {
  constructor(message: String?) : this(message, null)
  constructor(cause: Throwable?) : this(cause?.toString(), cause)
  constructor() : this(null, null)
}
