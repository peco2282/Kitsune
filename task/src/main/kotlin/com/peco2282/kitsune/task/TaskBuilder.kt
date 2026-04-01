package com.peco2282.kitsune.task

import java.util.concurrent.ScheduledFuture

class TaskBuilder(val scheduler: Scheduler) {
  private val tasks = mutableListOf<ScheduledFuture<*>>()

  /**
   * Schedules a task to run after the specified [delay].
   *
   * @param delay the amount of time to wait before running the task
   * @return a [DelayedTask] instance to continue the DSL
   */
  infix fun after(delay: Time) = DelayedTask(delay)

  /**
   * Runs the [task] on the next tick (synchronously on the main thread).
   *
   * @param task the block of code to execute
   * @return a [TaskHandle] that can be used to cancel the task
   */
  infix fun now(task: () -> Unit) = scheduler.now(task)

  /**
   * Runs the [task] asynchronously.
   *
   * @param task the block of code to execute
   * @return a [TaskHandle] that can be used to cancel the task
   */
  infix fun async(task: () -> Unit) = scheduler.async(task)

  /**
   * Represents a task that is set to run after a certain delay.
   *
   * @property delay the delay before execution
   */
  inner class DelayedTask(
    private val delay: Time
  ) : Runner {
    /**
     * Schedules this task to repeat with the specified [period].
     *
     * @param period the interval between subsequent executions
     * @return a [RepeatingTask] instance to continue the DSL
     */
    infix fun every(period: Time) = RepeatingTask(delay, period)

    /**
     * Executes the [task] once after the [delay].
     *
     * @param task the block of code to execute
     * @return a [TaskHandler] that can be used to cancel the task
     */
    override infix fun run(task: () -> Unit): TaskHandler =
      scheduler.later(delay, task)
  }

  /**
   * Represents a task that is set to run repeatedly.
   *
   * @property delay the initial delay before the first execution
   * @property period the interval between subsequent executions
   */
  inner class RepeatingTask(
    private val delay: Time,
    private val period: Time
  ) : Runner {
    /**
     * Executes the [task] repeatedly according to the [delay] and [period].
     *
     * @param task the block of code to execute
     * @return a [TaskHandle] that can be used to cancel the task
     */
    override infix fun run(task: () -> Unit): TaskHandler =
      scheduler.timer(delay, period, task)
  }
}
