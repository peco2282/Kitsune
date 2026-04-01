package com.peco2282.kitsune.reflect.impl.java

import com.peco2282.kitsune.reflect.*
import com.peco2282.kitsune.reflect.Target
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty

internal class JavaFieldAcessor<T>(private val target: Target<T>) : FieldAccessor<T> {
  private val fieldCache = ConcurrentHashMap<String, Field>()
  private val instance = target.instance

  override fun target(): Target<T> = target

  @Throws(ReflectionException::class)
  override fun get(name: String): Any? {
    val field = fieldCache.computeIfAbsent(name) {
      tryGetField(name, target.clazz).apply { isAccessible = true }
    }
    return tryGet { field[instance] }
  }

  @Throws(ReflectionException::class)
  override fun getValue(thisRef: Any?, props: KProperty<*>): Any? {
    return get(props.name)
  }

  @Throws(ReflectionException::class)
  override fun set(name: String, value: Any?) {
    val field = fieldCache.computeIfAbsent(name) {
      tryGetField(name, target.clazz).apply { isAccessible = true }
    }
    tryGet { field[instance] = value }
  }

  @Throws(ReflectionException::class)
  override fun setValue(thisRef: Any?, props: KProperty<*>, value: Any?) {
    set(props.name, value)
  }
}
