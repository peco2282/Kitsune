package com.peco2282.kitsune.reflect

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

class KotlinReflectionTest {
  @Suppress("unused")
  class KotlinTestClass(var value: String) {
    constructor() : this("default")
    constructor(int: Int) : this("int: $int")

    fun greet(name: String): String = "Hello, $name! value=$value"
    fun greet(): String = "Hello! value=$value"
  }

  @Test
  fun testKotlinFieldAccess() {
    val obj = KotlinTestClass("initial")
    // KClass.reflect() or Target(kclass) creates a target with isJava = false
    obj::class.reflect()
    // But Target(kclass) sets instance to null.
    // We need an instance target for non-static fields.
    // Let's create a Target with instance and isJava = false
    val instanceTarget = Target(obj, false)

    assertEquals("initial", instanceTarget["value"])
    instanceTarget["value"] = "updated"
    assertEquals("updated", obj.value)
  }

  @Test
  fun testKotlinMethodInvoke() {
    val obj = KotlinTestClass("method")
    val target = obj.reflect(false) // Use extension with isJava parameter

    assertEquals("Hello, Kotlin! value=method", target("greet", "Kotlin"))
    assertEquals("Hello! value=method", target("greet"))
  }

  @Suppress("unused")
  class OverloadTest {
    fun test(i: Int) = "Int: $i"
    fun test(s: String) = "String: $s"
  }

  @Test
  fun testMethodOverload() {
    val target = OverloadTest().reflect(false)
    assertEquals("Int: 123", target("test", 123))
    assertEquals("String: hello", target("test", "hello"))
  }

  @Test
  fun testKotlinConstructor() {
    val kclass: KClass<KotlinTestClass> = KotlinTestClass::class
    // KClass.reflect() now uses Kotlin mode by default
    val target = kclass.reflect()

    val obj1: KotlinTestClass = target.instantiate()
    assertEquals("default", obj1.value)

    val obj2: KotlinTestClass = target.instantiate(456)
    assertEquals("int: 456", obj2.value)
  }

  @Suppress("unused")
  class CompanionTest {
    companion object {
      var status = "off"
      fun toggle() {
        status = if (status == "off") "on" else "off"
      }
    }
  }

  @Test
  fun testCompanionObject() {
    val target = CompanionTest::class.reflect()
    val companion = target.companion ?: throw AssertionError("Companion object not found")

    assertEquals("off", companion["status"])
    companion("toggle")
    assertEquals("on", CompanionTest.status)
    companion["status"] = "custom"
    assertEquals("custom", CompanionTest.status)
  }

  @Test
  fun testModeSwitching() {
    val obj = KotlinTestClass("mode")
    val target = obj.reflect() // Default is Java mode

    assert(target.isJava)
    val kTarget = target.asKotlin()
    assert(kTarget.isKotlin)
    assertEquals("Hello! value=mode", kTarget("greet"))

    val jTarget = kTarget.asJava()
    assert(jTarget.isJava)
    assertEquals("Hello! value=mode", jTarget("greet"))
  }
}
