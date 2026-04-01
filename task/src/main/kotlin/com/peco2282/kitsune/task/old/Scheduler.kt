package com.peco2282.kitsune.task.old

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Scheduler {
  private val scheduler = Executors.newSingleThreadScheduledExecutor()
  private val scheduledTasks = mutableMapOf<String, ScheduledFuture<*>>()

  fun schedule(block: TaskBuilder.() -> Unit) {
    val builder = TaskBuilder().apply(block)
    val future = scheduler.scheduleAtFixedRate(
      builder.task::run,
      0,
      builder.period,
      builder.unit
    )
    scheduledTasks[builder.task.javaClass.name] = future
  }

  fun schedule(task: ScheduledTask, period: Long) {
    val future = scheduler.scheduleAtFixedRate(task::run, 0, period, TimeUnit.SECONDS)
    scheduledTasks[task.javaClass.name] = future
  }

  fun cancel(taskName: String) {
    scheduledTasks[taskName]?.cancel(true)
    scheduledTasks.remove(taskName)
  }

  fun shutdown() {
    scheduler.shutdown()
  }
}
