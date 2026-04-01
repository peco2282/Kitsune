package com.peco2282.kitsune.reflect

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChainedAccessorTest {
  class Sample(var name: String, var age: Int) {
    fun update(newName: String, newAge: Int) {
      name = newName
      age = newAge
    }
  }

  @Test
  fun testChainedFieldAccess() {
    val sample = Sample("Alice", 20)
    val target = Target(sample)

    val chain = target.chain()
    val container =
      chain
        .field().set("name", "Bob").end()
        .and()
        .field().set("age", 25)

    // まだ実行されていないはず
    assertEquals("Alice", sample.name)
    assertEquals(20, sample.age)

    container.run()

    assertEquals("Bob", sample.name)
    assertEquals(25, sample.age)
  }

  @Test
  fun testMixedChainedAccess() {
    val sample = Sample("Alice", 20)
    val target = Target(sample)

    target.chain()
      .field().set("name", "Dave").end().and()
      .method().invoke("update", "Eve", 40).end().and()
      .field().set("age", 45)
      .run()

    assertEquals("Eve", sample.name)
    assertEquals(45, sample.age)
  }
}
