package com.peco2282.kitsune.task

import java.util.concurrent.ScheduledFuture

interface TaskHandler {
  fun isStarted(): Boolean
  fun cancel(): Boolean
  fun future(): ScheduledFuture<*>
}

fun interface Runner {
  infix fun run(task: () -> Unit): TaskHandler
}

internal class TaskHandlerImpl(val future: ScheduledFuture<*>) : TaskHandler {
  override fun isStarted(): Boolean = !future.isDone

  override fun cancel() = future.cancel(true)

  override fun future(): ScheduledFuture<*> = future
}
