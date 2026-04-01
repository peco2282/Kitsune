package com.peco2282.kitsune.reflect

@DslMarker
annotation class ReflectDsl

@ReflectDsl
interface ReflectDSL<T> {
  fun field(name: String, accessor: FieldAccessor<*>.() -> Unit): ReflectDSL<T>
  fun method(name: String, accessor: MethodAccessor<*>.() -> Unit): ReflectDSL<T>
}



fun <T : Any> reflect(target: T, isJava: Boolean = false, dsl: ReflectDSL<T>.() -> Unit): Target<T> = Target(target, isJava)
