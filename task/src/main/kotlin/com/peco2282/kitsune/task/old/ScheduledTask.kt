package com.peco2282.kitsune.task.old

@FunctionalInterface
fun interface ScheduledTask : Runnable {
  override fun run()

  fun and(vararg tasks: ScheduledTask): ScheduledTask = ScheduledTask {
    this.run()
    tasks.forEach(ScheduledTask::run)
  }

  operator fun plus(task: ScheduledTask): ScheduledTask = and(task)
  operator fun plusAssign(task: ScheduledTask) {
    and(task)
  }
}
