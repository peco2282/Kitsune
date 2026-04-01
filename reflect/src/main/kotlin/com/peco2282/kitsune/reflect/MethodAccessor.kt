package com.peco2282.kitsune.reflect

interface MethodAccessor<T> {
  fun target(): Target<T>

  operator fun invoke(name: String, vararg args: Any): Any?

  interface ConstructorAccessor<T> {
    fun target(): Target<T>
    operator fun invoke(vararg args: Any): T
  }
}
