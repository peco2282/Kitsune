package com.peco2282.kitsune.collection

import java.util.*
import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collector

fun <T> MutableCollection<T>.toImmutable(): Collection<T> = Collections.unmodifiableCollection(this)
fun <T> MutableList<T>.toImmutable(): List<T> = Collections.unmodifiableList(this)
fun <T> MutableSet<T>.toImmutable(): Set<T> = Collections.unmodifiableSet(this)
fun <K, V> MutableMap<K, V>.toImmutable(): Map<K, V> = Collections.unmodifiableMap(this)

fun <T> List<T>.toMutable(): MutableList<T> = this as? MutableList<T> ?: ArrayList(this)
fun <T> Set<T>.toMutable(): MutableSet<T> = this as? MutableSet<T> ?: HashSet(this)
fun <K, V> Map<K, V>.toMutable(): MutableMap<K, V> = this as? MutableMap<K, V> ?: HashMap(this)

fun <T, MS : MutableSet<T>> set(
  sup: Supplier<MS>,
): Collector<T, MS, MS> {
  return InternalCollector(
    sup,
    MutableSet<T>::add,
    object : BinaryOperator<MS> {
      fun addAnd(set: MS, t: MS): MS = set.apply { addAll(t) }
      override fun apply(t: MS, u: MS): MS = if (t.size < u.size)
        addAnd(u, t) else addAnd(t, u)
    }
  )
}

fun <T, ML : MutableList<T>> list(
  sup: Supplier<ML>
): Collector<T, ML, ML> {
  return InternalCollector(
    sup,
    MutableList<T>::add,
    object : BinaryOperator<ML> {
      fun addAnd(list: ML, t: ML): ML = list.apply { addAll(t) }
      override fun apply(t: ML, u: ML): ML = if (t.size < u.size)
        addAnd(u, t) else addAnd(t, u)
    }
  )
}

fun <T, K, V, MM : MutableMap<K, V>> map(
  supplier: Supplier<MM>,
  keyMapper: Function<T, K>,
  valueMapper: Function<T, V>
): Collector<T, MM, MM> {
  return InternalCollector(
    supplier,
    { m, t -> m[keyMapper.apply(t)] = valueMapper.apply(t) },
    { m1, m2 -> if (m1.size < m2.size) m2.apply { putAll(m1) } else m1.apply { putAll(m2) } }
  )
}

internal class InternalCollector<T, O>(
  private val supplier: Supplier<O>,
  private val accumulator: BiConsumer<O, T>,
  private val combiner: BinaryOperator<O>
) : Collector<T, O, O> {
  override fun supplier(): Supplier<O> = supplier

  override fun accumulator(): BiConsumer<O, T> = accumulator

  override fun combiner(): BinaryOperator<O> = combiner

  override fun finisher(): Function<O, O> = Function.identity()

  override fun characteristics(): Set<Collector.Characteristics> =
    EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH)

}
