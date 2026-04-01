package com.peco2282.kitsune.reflect.impl.java

import com.peco2282.kitsune.reflect.MethodAccessor
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.tryGet
import com.peco2282.kitsune.reflect.tryGetConstructor
import java.lang.reflect.Constructor
import java.util.concurrent.ConcurrentHashMap

internal class JavaConstructorAccessor<T>(private val target: Target<T>) : MethodAccessor.ConstructorAccessor<T> {
  private val constructorCache = ConcurrentHashMap<List<Class<*>>, Constructor<*>>()

  override fun target(): Target<T> = target

  override fun invoke(vararg args: Any): T {
    val argTypes = args.map { arg ->
      @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "RemoveRedundantQualifierName")
      when (val clazz = arg.javaClass) {
        Integer::class.java -> Int::class.java
        java.lang.Long::class.java -> Long::class.java
        java.lang.Double::class.java -> Double::class.java
        java.lang.Float::class.java -> Float::class.java
        java.lang.Boolean::class.java -> Boolean::class.java
        java.lang.Byte::class.java -> Byte::class.java
        java.lang.Short::class.java -> Short::class.java
        Character::class.java -> Char::class.java
        else -> clazz
      }
    }
    val constructor = constructorCache.computeIfAbsent(argTypes) {
      val constructor = tryGetConstructor(target.clazz, *argTypes.toTypedArray())
      constructor.apply { isAccessible = true }
    }
    @Suppress("UNCHECKED_CAST")
    return tryGet { constructor.newInstance(*args) as T }
  }
}
