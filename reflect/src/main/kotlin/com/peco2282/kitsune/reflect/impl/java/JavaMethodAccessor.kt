package com.peco2282.kitsune.reflect.impl.java

import com.peco2282.kitsune.reflect.MethodAccessor
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.tryGet
import com.peco2282.kitsune.reflect.tryGetMethod
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

internal class JavaMethodAccessor<T>(private val target: Target<T>) : MethodAccessor<T> {
  private val methodCache = ConcurrentHashMap<MethodKey, Method>()
  private val instance = target.instance

  override fun target(): Target<T> = target

  private data class MethodKey(val name: String, val argTypes: List<Class<*>>)

  override fun invoke(name: String, vararg args: Any): Any? {
    val argTypes = args.map { arg ->
      @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "RemoveRedundantQualifierName")
      when (val clazz = arg.javaClass) {
        java.lang.Integer::class.java -> Int::class.java
        java.lang.Long::class.java -> Long::class.java
        java.lang.Double::class.java -> Double::class.java
        java.lang.Float::class.java -> Float::class.java
        java.lang.Boolean::class.java -> Boolean::class.java
        java.lang.Byte::class.java -> Byte::class.java
        java.lang.Short::class.java -> Short::class.java
        java.lang.Character::class.java -> Char::class.java
        else -> clazz
      }
    }
    val key = MethodKey(name, argTypes)
    val method = methodCache.computeIfAbsent(key) {
      val method = tryGetMethod(name, target.clazz, *argTypes.toTypedArray())
      method.apply { isAccessible = true }
    }
    return tryGet { method(instance, *args) }
  }
}
