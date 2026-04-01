package com.peco2282.kitsune.task.old

import java.util.concurrent.TimeUnit

@DslMarker
annotation class ScheduledDSLMarker

@ScheduledDSLMarker
internal class ScheduledDSLImpl(
  override var task: ScheduledTask = ScheduledTask {},
  override var period: Long = 0L,
  override var unit: TimeUnit = TimeUnit.SECONDS
) : ScheduledDSL

fun scheduled(dsl: ScheduledDSL.() -> Unit) {
  val impl = ScheduledDSLImpl().apply(dsl)
  Scheduler().schedule {
    task = impl.task
    period = impl.period
    unit = impl.unit
  }
}

fun schedule(block: TaskBuilder.() -> Unit) = Scheduler().apply { schedule(block) }

fun main() {
  val scheduler = Scheduler()
  val loggerTask = ScheduledTask { println("Hello, world!") }

  // DSLを使ってタスクをスケジュールする
  scheduler.schedule {
    every(3).seconds(loggerTask)
  }

  // 10秒後にタスクをキャンセルする
  Thread.sleep(10000)
  println("10秒経過...タスクをキャンセルします。")
  scheduler.cancel(loggerTask.javaClass.name)

  // スケジューラをシャットダウン
  scheduler.shutdown()
}
