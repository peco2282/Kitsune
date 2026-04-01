package com.peco2282.kitsune.injector

import kotlin.test.*

class InjectorTest {

    interface MyService {
        fun greet(): String
    }

    class MyServiceImpl : MyService {
        override fun greet() = "Hello"
    }

    class Target {
        @Inject
        lateinit var service: MyService
    }

    @BeforeTest
    fun setup() {
        Injector.clear()
    }

    @Test
    fun testRegisterAndGet() {
        val service = MyServiceImpl()
        Injector.register(MyService::class, service)

        val retrieved = Injector.get(MyService::class)
        assertEquals(service, retrieved)
    }

    @Test
    fun testRegisterInlineAndGet() {
        val service = MyServiceImpl()
        Injector.register<MyService>(service)

        val retrieved = Injector.get<MyService>()
        assertEquals(service, retrieved)
    }

    @Test
    fun testGetOrNull() {
        assertNull(Injector.getOrNull<MyService>())

        val service = MyServiceImpl()
        Injector.register<MyService>(service)
        assertNotNull(Injector.getOrNull<MyService>())
    }

    @Test
    fun testInject() {
        val service = MyServiceImpl()
        Injector.register(MyService::class, service)

        val target = Target()
        Injector.inject(target)

        assertEquals("Hello", target.service.greet())
    }

    @Test
    fun testDelegateInject() {
        val service = MyServiceImpl()
        Injector.register<MyService>(service)

        class DelegateTarget {
            val service: MyService by Injector.inject()
        }

        val target = DelegateTarget()
        assertEquals("Hello", target.service.greet())
    }

    @Test
    fun testRunServices() {
        var called = false
        val service = Service { called = true }
        Injector.register(service)
        Injector.runServices()
        assertTrue(called)
    }

    @Test
    fun testBindAndGetOrCreate() {
        Injector.bind<MyService, MyServiceImpl>()
        val service = Injector.getOrCreate<MyService>()
        assertTrue(service is MyServiceImpl)
        assertEquals("Hello", service.greet())
    }

    @Test
    fun testConstructorInjection() {
        class Dependency
        class Dependent(val dependency: Dependency)

        Injector.register(Dependency())
        val instance = Injector.createInstance(Dependent::class)
        assertNotNull(instance.dependency)
    }

    @Test
    fun testNotFound() {
        assertFailsWith<NoSuchElementException> {
            Injector.get<MyService>()
        }
    }
}
