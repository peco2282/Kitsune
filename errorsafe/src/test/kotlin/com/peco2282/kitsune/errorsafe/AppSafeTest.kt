package com.peco2282.kitsune.errorsafe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppSafeTest {

  @Test
  fun testSuccessChain() {
    var resultValue: String? = null
    var successCalled = false
    class Value(val v: String)

    AppSafe.safe {
      "Hello"
    }.onSuccessListener {
      resultValue = it
      successCalled = true
    }.onErrorListener {
      throw IllegalStateException("Should not be called")
    }.then {
      Value(it)
    }.onSuccessListener { 
      assertEquals("Hello", it.v)
    }.apply()

    assertTrue(successCalled)
    assertEquals("Hello", resultValue)
  }

  @Test
  fun testErrorChain() {
    var errorCaught: Throwable? = null
    var errorCalled = false

    AppSafe.safe {
      throw RuntimeException("Expected error")
    }.onSuccessListener {
      throw IllegalStateException("Should not be called")
    }.onErrorListener {
      errorCaught = it
      errorCalled = true
    }.then {
      throw IllegalStateException("Should not be called")
    }.onErrorListener {
      errorCaught = it
      errorCalled = true
    }.apply()

    assertTrue(errorCalled)
    assertEquals("Expected error", errorCaught?.message)
  }

  @Test
  fun testSpecificErrorChain() {
    var caughtSpecific = false
    var caughtGeneral = false

    AppSafe.safe {
      throw IllegalArgumentException("Specific error")
    }.onErrorListener(IllegalArgumentException::class.java) {
      caughtSpecific = true
    }.onErrorListener {
      caughtGeneral = true
    }.apply()

    assertTrue(caughtSpecific)
    assertTrue(caughtGeneral)
  }
}
