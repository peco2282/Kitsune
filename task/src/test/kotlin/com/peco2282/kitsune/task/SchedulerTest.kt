package com.peco2282.kitsune.task

import kotlin.test.Test

class SchedulerTest {
  @Test
  fun `task creator`() {
    val scheduler = Scheduler()

    val t1 = scheduler.createTask
    val t2 = scheduler.createTask
    println(t1.hashCode())
    println(t2.hashCode())
  }
}
