package com.peco2282.kitsune.reflect.impl

import com.peco2282.kitsune.reflect.ChainedAccessor
import com.peco2282.kitsune.reflect.Holder
import com.peco2282.kitsune.reflect.Target
import com.peco2282.kitsune.reflect.Task

internal class Impl<T>(val target: Target<T>, val tasks: Holder<Task> = Holder.Companion.of()) : ChainedAccessor {
  override fun field(): ChainedAccessor.Field<T> = FImpl(target, this, tasks)

  override fun method(): ChainedAccessor.Method<T> = MImpl(target, this, tasks)

  data class EndImpl(
    val accessor: ChainedAccessor
  ) : ChainedAccessor.End {
    constructor(container: ChainedAccessor.Container) : this(container.accessor)

    override fun and(): ChainedAccessor = accessor
  }

  data class FContImpl<T>(
    val target: Target<T>,
    override val accessor: ChainedAccessor,
    override val action: Holder<Task>
  ) : ChainedAccessor.FieldContainer {
    override fun end(): ChainedAccessor.End = EndImpl(this)
    override fun run() {
      action.get()?.run()
    }
  }

  data class MContImpl<T>(
    val target: Target<T>,
    override val accessor: ChainedAccessor,
    override val action: Holder<Task>
  ) : ChainedAccessor.MethodContainer {
    override fun end(): ChainedAccessor.End = EndImpl(this)
    override fun run() {
      action.get()?.run()
    }
  }


  class FImpl<T>(
    val target: Target<T>,
    val accessor: ChainedAccessor,
    private val tasks: Holder<Task>
  ) : ChainedAccessor.Field<T> {
    override fun target() = target

    override fun get(name: String): ChainedAccessor.FieldContainer {
      tasks.compute { current ->
        val next = Task {
          target.field()[name]
        }
        current?.and(next) ?: next
      }
      return FContImpl(target, accessor, tasks)
    }

    override fun set(name: String, value: Any?): ChainedAccessor.FieldContainer {
      tasks.compute { current ->
        val next = Task {
          target.field()[name] = value
        }
        current?.and(next) ?: next
      }
      return FContImpl(target, accessor, tasks)
    }

    override fun end(): ChainedAccessor.End = EndImpl(this.accessor)
  }

  class MImpl<T>(
    val target: Target<T>,
    val accessor: ChainedAccessor,
    private val tasks: Holder<Task>
  ) : ChainedAccessor.Method<T> {
    override fun target() = target
    override fun end(): ChainedAccessor.End = EndImpl(accessor)

    override fun invoke(name: String, vararg args: Any): ChainedAccessor.MethodContainer {
      tasks.compute { current ->
        val next = Task {
          target.method()(name, *args)
        }
        current?.and(next) ?: next
      }
      return MContImpl(target, accessor, tasks)
    }
  }
}
