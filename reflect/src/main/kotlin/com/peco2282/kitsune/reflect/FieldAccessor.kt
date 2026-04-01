package com.peco2282.kitsune.reflect

import kotlin.reflect.KProperty

interface FieldAccessor<T> {
  fun target(): Target<T>
  operator fun get(name: String): Any?
  operator fun getValue(thisRef: Any?, props: KProperty<*>): Any?
  operator fun set(name: String, value: Any?)
  operator fun setValue(thisRef: Any?, props: KProperty<*>, value: Any?)
}
