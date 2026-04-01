package com.peco2282.kitsune.reflect

import com.peco2282.kitsune.reflect.impl.Impl
import com.peco2282.kitsune.reflect.impl.java.JavaConstructorAccessor
import com.peco2282.kitsune.reflect.impl.java.JavaFieldAcessor
import com.peco2282.kitsune.reflect.impl.java.JavaMethodAccessor
import com.peco2282.kitsune.reflect.impl.kotlin.KotlinConstructorAccessor
import com.peco2282.kitsune.reflect.impl.kotlin.KotlinFieldAccessor
import com.peco2282.kitsune.reflect.impl.kotlin.KotlinMethodAccessor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObjectInstance

class Target<T> internal constructor(val clazz: Class<out T & Any>, val instance: T, val isJava: Boolean) {
  val isKotlin: Boolean get() = !isJava

  constructor(instance: T & Any, isJava: Boolean) : this(instance::class.java, instance, isJava)
  constructor(instance: T & Any) : this(instance, true)
  constructor(clazz: Class<out T & Any>, isJava: Boolean) : this(
    clazz,
    @Suppress("UNCHECKED_CAST") (null as Any? as T),
    isJava
  )

  constructor(clazz: Class<out T & Any>) : this(clazz, true)
  constructor(kclass: KClass<out T & Any>) : this(kclass.java, true)

  /**
   * Kotlin モードの Target に変換します。
   */
  fun asKotlin(): Target<T> = if (isKotlin) this else Target(clazz, instance, false)

  /**
   * Java モードの Target に変換します。
   */
  fun asJava(): Target<T> = if (isJava) this else Target(clazz, instance, true)

  /**
   * コンパニオンオブジェクトを Target として取得します。
   */
  val companion: Target<Any?>?
    get() {
      val companionObject = clazz.kotlin.companionObjectInstance ?: return null
      return Target(companionObject, isJava)
    }

  fun field(): FieldAccessor<T> {
    if (isJava) {
      return JavaFieldAcessor(this)
    }
    return KotlinFieldAccessor(this)
  }

  fun chain(): ChainedAccessor = Impl(this)

  fun method(): MethodAccessor<T> {
    if (isJava) {
      return JavaMethodAccessor(this)
    }
    return KotlinMethodAccessor(this)
  }

  fun constructor(): MethodAccessor.ConstructorAccessor<T> {
    if (isJava) {
      return JavaConstructorAccessor(this)
    }
    return KotlinConstructorAccessor(this)
  }

  internal fun name(): String = clazz.name

  operator fun get(name: String): Any? = field()[name]
  operator fun set(name: String, value: Any?) = field().set(name, value)
  operator fun invoke(name: String, vararg args: Any): Any? = method()(name, *args)

  /**
   * フィールド名でプロパティ委譲を行うためのデリゲートを作成します。
   *
   * @param name フィールド名
   * @return [FieldDelegate] インスタンス
   */
  fun <R> delegate(name: String): FieldDelegate<R> = FieldDelegate(this, name)

  /**
   * プロパティ委譲のためのデリゲートクラス。
   */
  class FieldDelegate<R>(val target: Target<*>, val name: String) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, props: KProperty<*>): R = target.field()[name] as R
    operator fun setValue(thisRef: Any?, props: KProperty<*>, value: R) = target.field().set(name, value)
  }

  /**
   * インスタンスを生成します。
   *
   * @param args コンストラクタ引数
   * @return 生成されたインスタンス
   */
  fun <R> instantiate(vararg args: Any): R {
    @Suppress("UNCHECKED_CAST")
    return constructor()(*args) as R
  }
}

interface TargetAccessor<T> {
  fun target(): Target<T>
}

/**
 * 対象のインスタンスに対してリフレクション操作を行うための [Target] を作成します。
 */
fun <T : Any> T.reflect(isJava: Boolean = true): Target<T> = Target(this, isJava)

/**
 * 対象のクラスに対してリフレクション操作を行うための [Target] を作成します。
 */
fun <T : Any> Class<T>.reflect(isJava: Boolean = true): Target<T?> = Target(this, isJava)

/**
 * 対象の [KClass] に対してリフレクション操作を行うための [Target] を作成します。
 */
fun <T : Any> KClass<T>.reflect(isJava: Boolean = false): Target<T?> = Target(this.java, isJava)
