package com.peco2282.kitsune.task.old

import java.util.concurrent.TimeUnit

class TaskBuilder {
  var period: Long = 0L
  var unit: TimeUnit = TimeUnit.SECONDS
  lateinit var task: ScheduledTask

  fun every(period: Long): TaskBuilder {
    this.period = period
    return this
  }

  // 単位を設定する
  infix fun seconds(task: ScheduledTask) {
    this.unit = TimeUnit.SECONDS
    this.task = task
  }

  infix fun minutes(task: ScheduledTask) {
    this.unit = TimeUnit.MINUTES
    this.task = task
  }
}
