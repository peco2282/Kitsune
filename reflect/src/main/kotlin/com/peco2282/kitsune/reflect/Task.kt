package com.peco2282.kitsune.reflect

fun interface Task {
  fun run()

  infix fun and(task: Task): Task = Task {
    this.run()
    task.run()
  }
}


