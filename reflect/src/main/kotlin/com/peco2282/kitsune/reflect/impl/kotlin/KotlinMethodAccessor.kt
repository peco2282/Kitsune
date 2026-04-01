package com.peco2282.kitsune.reflect.impl.kotlin

import com.peco2282.kitsune.reflect.MethodAccessor
import com.peco2282.kitsune.reflect.ReflectionException
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.tryGet
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible

internal class KotlinMethodAccessor<T>(private val target: Target<T>) : MethodAccessor<T> {
  private val instance = target.instance

  override fun target(): Target<T> = target

  override fun invoke(name: String, vararg args: Any): Any? {
    val kclass = target.clazz.kotlin
    // 名前が一致し、引数の数が一致する関数を探す
    // 厳密な型チェックは難しい（Kotlinリフレクションの型マッチングは複雑）
    // とりあえず名前と数でフィルタリング
    val functions = kclass.functions.filter { it.name == name && it.parameters.size == (if (instance != null) args.size + 1 else args.size) }
    
    val func = if (functions.size > 1) {
      // 複数の候補がある場合、引数の型でマッチングを試みる
      functions.find { f ->
        val params = if (instance != null) f.parameters.drop(1) else f.parameters
        params.zip(args).all { (p, a) ->
          val pClass = p.type.classifier as? KClass<*>
          pClass?.isInstance(a) ?: true
        }
      } ?: functions.first()
    } else {
      functions.firstOrNull()
    } ?: throw ReflectionException("Method $name with ${args.size} arguments not found in ${target.name()}")

    func.isAccessible = true
    return tryGet {
      func.call(instance, *args)
    }
  }
}
