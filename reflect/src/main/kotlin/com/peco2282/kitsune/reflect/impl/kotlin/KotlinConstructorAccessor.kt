package com.peco2282.kitsune.reflect.impl.kotlin

import com.peco2282.kitsune.reflect.MethodAccessor
import com.peco2282.kitsune.reflect.ReflectionException
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.tryGet
import kotlin.reflect.KClass
import kotlin.reflect.jvm.isAccessible

internal class KotlinConstructorAccessor<T>(private val target: Target<T>) : MethodAccessor.ConstructorAccessor<T> {
  override fun target(): Target<T> = target

  override fun invoke(vararg args: Any): T {
    val kclass = target.clazz.kotlin

    // とりあえず引数の数でマッチするコンストラクタを探す
    val constructors = kclass.constructors.filter { it.parameters.size == args.size }
    
    val constructor = if (constructors.size > 1) {
      // 複数の候補がある場合、引数の型でマッチングを試みる
      constructors.find { c ->
        c.parameters.zip(args).all { (p, a) ->
          val pClass = p.type.classifier as? KClass<*>
          pClass?.isInstance(a) ?: true
        }
      } ?: constructors.first()
    } else {
      constructors.firstOrNull()
    } ?: throw ReflectionException("Constructor with ${args.size} arguments not found in ${target.name()}")

    constructor.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    return tryGet {
      constructor.call(*args) as T
    }
  }
}
