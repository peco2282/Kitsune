package com.peco2282.kitsune.collection

import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.function.UnaryOperator
import java.util.stream.Stream

fun <T> Stream<T>.toSet(): MutableSet<T> = collect(set(::HashSet))

fun <T, K, V> Stream<T>.toMap(
  key: Function<T, K>,
  value: Function<T, V>
): MutableMap<K, V> = collect(map(::HashMap, key, value))

infix fun <T, SET : MutableSet<T>> Stream<T>.toSet(supplier: Supplier<SET>): SET = collect(set(supplier))

infix fun <T, LIST : MutableList<T>> Stream<T>.toList(supplier: Supplier<LIST>): LIST = collect(list(supplier))

fun <T, K, V, MAP : MutableMap<K, V>> Stream<T>.toMap(
  supplier: Supplier<MAP>,
  key: Function<T, K>,
  value: Function<T, V>
): MAP = collect(map(supplier, key, value))

infix fun <T, U : T> Stream<T>.filter(clazz: Class<U>): Stream<U> = filter(clazz::isInstance).map(clazz::cast)

inline fun <T, reified U : T> Stream<T>.filterIsInstance(): Stream<U> =
  this.filter { it is U }
  .map { it as U }

@Suppress("UNCHECKED_CAST")
fun <T> Stream<T?>.filterNotNull(): Stream<T> = filter { it != null } as Stream<T>

fun <T> Stream<T>.forEachIndexed(action: (index: Int, T) -> Unit) {
  var index = 0
  this.forEach { item ->
    action(index++, item)
  }
}

fun <T> Stream<T>.onEach(action: (T) -> Unit): Stream<T> = this.peek(action)

fun <T> Stream<T>.mapIf(condition: Predicate<T>, mapper: UnaryOperator<T>): Stream<T> =
  this.map { if (condition.test(it)) mapper.apply(it) else it }

@Suppress("UNCHECKED_CAST")
fun <T, U: T> Stream<T>.cast(): Stream<U> = map { it as U }
