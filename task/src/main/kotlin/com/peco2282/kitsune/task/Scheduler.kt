package com.peco2282.kitsune.task

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Scheduler {
  val service: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
  private val tasks = mutableListOf<ScheduledFuture<*>>()

  val createTask: TaskBuilder
    get() = TaskBuilder(this)

  private fun wrap(task: () -> Unit): Runnable = Runnable {
    try {
      task()
    } catch (e: Exception) {
      val error = Error(e)
    }
  }

  fun later(time: Time, task: () -> Unit): TaskHandler {
    val future = service.schedule(task, time.milliseconds, TimeUnit.MILLISECONDS)
    tasks += future
    return TaskHandlerImpl(future)
  }

  fun now(task: () -> Unit) = later(Time.IMMEDIATELY, task)
  fun timer(delay: Time, period: Time, task: () -> Unit): TaskHandler {
    val future = service.scheduleAtFixedRate(task, delay.milliseconds, period.milliseconds, TimeUnit.MILLISECONDS)
    tasks += future
    return TaskHandlerImpl(future)
  }
  fun async(task: () -> Unit) = service.submit(wrap(task))
}
