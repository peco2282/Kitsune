package com.peco2282.kitsune.collection

import java.util.Optional

fun <T, K> Iterable<T>.distinctBy(selector: (T) -> K): List<T> {
  val seen = HashSet<K>()
  return filter { item -> seen.add(selector(item)) }
}

fun <T> Iterable<T>.partitionToMap(predicate: (T) -> Boolean): Map<Boolean, List<T>> {
  val trueList = mutableListOf<T>()
  val falseList = mutableListOf<T>()

  for (item in this) {
    if (predicate(item)) {
      trueList.add(item)
    } else {
      falseList.add(item)
    }
  }

  return mapOf(true to trueList, false to falseList)
}

fun <T> Iterable<T>.averageBy(selector: (T) -> Number): Double {
  var sum = 0.0
  var count = 0

  for (item in this) {
    sum += selector(item).toDouble()
    count++
  }

  return if (count == 0) Double.NaN else sum / count
}

fun <T> Iterable<T>.applyIf(condition: Boolean, transform: (Iterable<T>) -> Iterable<T>): Iterable<T> {
  return if (condition) {
    transform(this)
  } else {
    this
  }
}

fun <T> Iterable<T>.batch(size: Int, partialWindow: Boolean = true): Sequence<List<T>> {
  require(size > 0) { "Batch size must be positive." }

  return sequence {
    val iterator = this@batch.iterator()

    while (iterator.hasNext()) {
      val chunk = mutableListOf<T>()
      repeat(size) {
        if (iterator.hasNext()) {
          chunk.add(iterator.next())
        }
      }
      if (chunk.isNotEmpty() && (chunk.size == size || partialWindow)) {
        yield(chunk)
      } else if (chunk.size < size && !partialWindow) {
        return@sequence
      }
    }
  }
}

fun <T> Iterable<T>.pairwise(): Sequence<Pair<T, T>> = sequence {
  val iterator = this@pairwise.iterator()
  if (!iterator.hasNext()) return@sequence

  var previous = iterator.next()

  while (iterator.hasNext()) {
    val current = iterator.next()
    yield(previous to current)
    previous = current
  }
}
fun <T> Iterable<Optional<T>>.filterAndUnwrap(): List<T> {
  return this.filter { it.isPresent }
    .map { it.get() }
}
fun <T> Iterable<T>.ifNotEmpty(transform: (Iterable<T>) -> Iterable<T>): Iterable<T> {
  return if (this.any()) {
    transform(this)
  } else {
    this
  }
}
fun <T, R> Iterable<T>.mapToResult(mapper: (T) -> R): List<Result<R>> {
  return this.map { element ->
    runCatching {
      mapper(element)
    }
  }
}
fun <T> Iterable<T>.associateWithIndex(): Map<Int, T> {
  val map = mutableMapOf<Int, T>()
  this.forEachIndexed { index, item ->
    map[index] = item
  }
  return map
}
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
  if (index1 == index2) return
  val element1 = this[index1]
  this[index1] = this[index2]
  this[index2] = element1
}
fun <T> MutableList<T>.rotate(distance: Int) {
  if (isEmpty() || distance == 0) return

  java.util.Collections.rotate(this, distance)
}
fun <T> MutableList<T>.mapInPlace(transform: (T) -> T) {
  val iterator = this.listIterator()
  while (iterator.hasNext()) {
    val original = iterator.next()
    val transformed = transform(original)
    iterator.set(transformed)
  }
}

fun <T> Iterable<T>.takeWhile(predicate: (T) -> Boolean): Sequence<T> {
  return this.asSequence().takeWhile(predicate)
}
