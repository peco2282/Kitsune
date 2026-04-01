package com.peco2282.kitsune.errorsafe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SafeResultDelayTest {

  @Test
  fun testDelayExecution() {
    var called = false
    val result = AppSafe.safe {
      called = true
      "Success"
    }

    assertFalse(called, "AppSafe.safe should not execute the block immediately")

    var listenerCalled = false
    val listener = result.onSuccessListener {
      listenerCalled = true
    }

    assertFalse(listenerCalled, "Listener should not be called before apply()")

    val value = listener.apply()
    assertTrue(called, "AppSafe.safe should be called after apply()")
    assertTrue(listenerCalled, "Listener should be called after apply()")
    assertEquals("Success", value, "apply() should return the result value")
  }

  @Test
  fun testThenChainDelay() {
    var thenCalled = false
    var finalSuccessCalled = false

    val chain = AppSafe.safe {
      "Start"
    }
      .then {
        thenCalled = true
        "$it -> Middle"
      }
      .onSuccessListener {
        finalSuccessCalled = true
      }

    assertFalse(thenCalled, "then() block should not be called before apply()")
    assertFalse(finalSuccessCalled, "Success listener should not be called before apply()")

    chain.apply()

    assertTrue(thenCalled, "then() block should be called after apply()")
    assertTrue(finalSuccessCalled, "Success listener should be called after apply()")
  }

  @Test
  fun testMultipleApply() {
    var callCount = 0
    val chain = AppSafe.safe { "Action" }
      .onSuccessListener {
        callCount++
      }

    chain.apply()
    assertEquals(1, callCount, "Should be called once")

    chain.apply()
    assertEquals(1, callCount, "Should not be called again (isApplied check)")
  }

  @Test
  fun testErrorDelay() {
    var errorCalled = false
    val chain = AppSafe.safe {
      throw RuntimeException("Error")
    }.onErrorListener {
      errorCalled = true
    }

    assertFalse(errorCalled, "Error listener should not be called before apply()")

    chain.apply()
    assertTrue(errorCalled, "Error listener should be called after apply()")
  }
}
