package com.peco2282.kitsune.reflect

import kotlin.reflect.KProperty

class Holder<T>(initialData: T?) {
  private val locker = Unit
  private var data: T? = synchronized(locker) { return@synchronized initialData }
  operator fun getValue(name: String, param: KProperty<*>): T? = synchronized(locker) { return@synchronized data }
  operator fun setValue(name: String, param: KProperty<*>, value: T?) = synchronized(locker) {
    data = value
  }


  fun get(): T? = synchronized(locker) { return data }
  fun set(value: T?) = synchronized(locker) {
    data = value
  }

  fun clear() {
    data = null
  }

  fun isNull(): Boolean = data == null
  fun isNotNull(): Boolean = data != null
  fun compute(f: (T?) -> T?): T? = f(data).also { data = it }
  fun computeIfNull(f: () -> T?) {
    if (isNull()) f()
  }

  fun computeIfNotNull(f: (T) -> T?) {
    if (isNotNull()) data = synchronized(locker) { f(data!!) }
  }

  companion object {
    fun <T> of(initialData: T?) = Holder(initialData)
    fun <T> of() = Holder<T>(null)
  }
}
