package com.peco2282.kitsune.syntactical

infix fun <T, R> T.andThen(f: (T) -> R): R = f(this)
suspend infix fun <T, R> T.andSuspend(f: suspend (T) -> R): R = f(this)

infix fun <K, V> MutableMap<K, V>.del(k: K) = remove(k)


internal suspend fun main() {
  val a = 10
  val b = a andSuspend { it + 10 } andSuspend { it * 2 }
  val c = a andThen { it + 10 } andThen { it * 2 }
  println(c)
}
