package com.peco2282.kitsune.reflect

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReflectionTest {

  class TestClass(var value: String) {
    constructor() : this("default")
    constructor(int: Int) : this("int: $int")

    fun sayHello(name: String): String = "Hello, $name! value=$value"
    fun sayHello(): String = "Hello! value=$value"

    companion object {
      @JvmStatic
      val staticValue = "static"

      @JvmStatic
      fun staticMethod() = "static result"
    }
  }

  open class Parent {
    var parentField = "parent"
    fun parentMethod() = "from parent"
  }

  class Child : Parent()

  @Test
  fun testBasicReflection() {
    val obj = TestClass("initial")
    val target = obj.reflect()

    // Field access
    assertEquals("initial", target.get("value"))
    target.set("value", "updated")
    assertEquals("updated", obj.value)

    // Method access (overload)
    assertEquals("Hello, world! value=updated", target.invoke("sayHello", "world"))
    assertEquals("Hello! value=updated", target.invoke("sayHello"))
  }

  @Test
  fun testDelegate() {
    val obj = TestClass("delegate")
    val target = obj.reflect()

    var value: String by target.delegate("value")
    assertEquals("delegate", value)

    value = "changed"
    assertEquals("changed", obj.value)
  }

  @Test
  fun testFieldAccessorDelegate() {
    val obj = TestClass("accessor")
    val field = obj.reflect().field()

    var value: Any? by field
    // Note: The property name 'value' matches the field name in TestClass
    assertEquals("accessor", value)

    value = "changed"
    assertEquals("changed", obj.value)
  }

  @Test
  fun testConstructor() {
    val target = TestClass::class.reflect(true)

    val obj1: TestClass = target.instantiate()
    assertEquals("default", obj1.value)

    val obj2: TestClass = target.instantiate(123)
    assertEquals("int: 123", obj2.value)
  }

  @Test
  fun testInheritance() {
    val child = Child()
    val target = child.reflect()

    assertEquals("parent", target.get("parentField"))
    assertEquals("from parent", target.invoke("parentMethod"))
  }

  @Test
  fun testStaticAccess() {
    val target = TestClass::class.reflect(true)

    // Static field (Java reflection allows accessing static fields via instance = null)
    // However, our current JavaFieldAcessor uses target.instance which is null.
    // In Java reflection Field.get(null) works for static fields.

    assertEquals("static", target.get("staticValue"))
    assertEquals("static result", target.invoke("staticMethod"))
  }
}
