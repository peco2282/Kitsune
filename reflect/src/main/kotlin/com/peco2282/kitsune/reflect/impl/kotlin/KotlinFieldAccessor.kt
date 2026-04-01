package com.peco2282.kitsune.reflect.impl.kotlin

import com.peco2282.kitsune.reflect.FieldAccessor
import com.peco2282.kitsune.reflect.ReflectionException
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.tryGet
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

internal class KotlinFieldAccessor<T>(private val target: Target<T>) : FieldAccessor<T> {
  private val instance = target.instance

  override fun target(): Target<T> = target

  private fun getProperty(name: String): KProperty1<Any, *> {
    val kclass = target.clazz.kotlin
    val prop = kclass.memberProperties.find { it.name == name }
      ?: throw ReflectionException("Property $name not found in ${target.name()}")
    @Suppress("UNCHECKED_CAST")
    return (prop as KProperty1<Any, *>).apply { isAccessible = true }
  }

  override fun get(name: String): Any? {
    val prop = getProperty(name)
    return tryGet { prop.get(instance as Any) }
  }

  override fun getValue(thisRef: Any?, props: KProperty<*>): Any? {
    return get(props.name)
  }

  override fun set(name: String, value: Any?) {
    val prop = getProperty(name)
    if (prop !is KMutableProperty1) {
      throw ReflectionException("Property $name is not mutable in ${target.name()}")
    }
    @Suppress("UNCHECKED_CAST")
    tryGet { (prop as KMutableProperty1<Any, Any?>).set(instance as Any, value) }
  }

  override fun setValue(thisRef: Any?, props: KProperty<*>, value: Any?) {
    set(props.name, value)
  }
}
