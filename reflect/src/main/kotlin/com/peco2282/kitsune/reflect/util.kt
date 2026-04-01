package com.peco2282.kitsune.reflect

import java.lang.reflect.Field
import java.lang.reflect.Method

@Throws(ReflectionException::class)
fun tryGetField(name: String, clazz: Class<*>): Field {
  return tryGet {
    var current: Class<*>? = clazz
    while (current != null) {
      try {
        return@tryGet current.getDeclaredField(name)
      } catch (e: NoSuchFieldException) {
        current = current.superclass
      }
    }
    throw NoSuchFieldException(name)
  }
}

@Throws(ReflectionException::class)
fun tryGetMethod(name: String, clazz: Class<*>, vararg params: Class<*>): Method {
  return tryGet {
    var current: Class<*>? = clazz
    while (current != null) {
      try {
        return@tryGet current.getDeclaredMethod(name, *params)
      } catch (e: NoSuchMethodException) {
        current = current.superclass
      }
    }
    throw NoSuchMethodException(name)
  }
}

@Throws(ReflectionException::class)
fun <T> tryGet(action: () -> T): T {
  return try {
    action()
  } catch (e: ReflectiveOperationException) {
    throw ReflectionException(e)
  }
}

@Throws(ReflectionException::class)
fun tryGetConstructor(clazz: Class<*>, vararg params: Class<*>): java.lang.reflect.Constructor<*> {
  return tryGet { clazz.getDeclaredConstructor(*params) }
}
