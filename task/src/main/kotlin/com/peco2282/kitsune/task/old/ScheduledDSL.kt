package com.peco2282.kitsune.task.old

import java.util.concurrent.TimeUnit

@ScheduledDSLMarker
interface ScheduledDSL {
  var task: ScheduledTask
  var period: Long
  var unit: TimeUnit
}
