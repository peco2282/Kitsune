package com.peco2282.kitsune.reflect

interface ChainedAccessor {
  fun field(): Field<*>
  fun method(): Method<*>

  interface End {
    fun and(): ChainedAccessor
  }

  interface Accessor<T>: TargetAccessor<T> {
    override fun target(): Target<T>
    fun end(): End
  }

  interface Field<T> : Accessor<T> {
    override fun target(): Target<T>

    operator fun get(name: String): FieldContainer
    operator fun set(name: String, value: Any?): FieldContainer
  }

  interface Method<T> : Accessor<T> {
    override fun target(): Target<T>

    operator fun invoke(name: String, vararg args: Any): MethodContainer
  }

  interface Container {
    val accessor: ChainedAccessor
    val action: Holder<Task>
    fun end(): End
    fun run()
  }

  interface FieldContainer : Container
  interface MethodContainer : Container
}
