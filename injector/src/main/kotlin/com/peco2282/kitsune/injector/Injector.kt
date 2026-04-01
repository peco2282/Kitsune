package com.peco2282.kitsune.injector

import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * サービスを管理・注入するためのインジェクター。
 */
object Injector {
  private val services = ConcurrentHashMap<KClass<*>, Any>()
  private val bindings = ConcurrentHashMap<KClass<*>, KClass<*>>()

  /**
   * インターフェースを実装クラスにバインドします。
   *
   * @param interfaceClass インターフェースのクラス
   * @param implementationClass 実装クラス
   */
  fun <I : Any, T : I> bind(interfaceClass: KClass<I>, implementationClass: KClass<T>) {
    bindings[interfaceClass] = implementationClass
  }

  /**
   * インターフェースを実装クラスにバインドします。
   */
  inline fun <reified I : Any, reified T : I> bind() {
    bind(I::class, T::class)
  }

  /**
   * 指定されたクラスのサービスを登録します。
   *
   * @param clazz 登録するクラス
   * @param service 登録するインスタンス
   */
  fun <T : Any> register(clazz: KClass<T>, service: T) {
    services[clazz] = service
  }

  /**
   * 指定されたクラスのサービスを登録します。
   *
   * @param service 登録するインスタンス（そのクラス自身で登録されます）
   */
  inline fun <reified T : Any> register(service: T) {
    register(T::class, service)
  }

  /**
   * 指定されたクラスのサービスを取得します。
   *
   * @param clazz 取得するクラス
   * @return 登録されているインスタンス
   * @throws NoSuchElementException サービスが見つからない場合
   */
  @Suppress("UNCHECKED_CAST")
  operator fun <T : Any> get(clazz: KClass<T>): T {
    return services[clazz] as? T ?: throw NoSuchElementException("Service not found for ${clazz.qualifiedName}")
  }

  /**
   * 指定されたクラスのサービスを取得します。
   *
   * @return 登録されているインスタンス
   * @throws NoSuchElementException サービスが見つからない場合
   */
  inline fun <reified T : Any> get(): T {
    return get(T::class)
  }

  /**
   * 指定されたクラスのサービスを取得します。登録されていない場合は null を返します。
   *
   * @param clazz 取得するクラス
   * @return 登録されているインスタンス、または null
   */
  @Suppress("UNCHECKED_CAST")
  fun <T : Any> getOrNull(clazz: KClass<T>): T? {
    return services[clazz] as? T
  }

  /**
   * 指定されたクラスのサービスを取得します。登録されていない場合は null を返します。
   *
   * @return 登録されているインスタンス、または null
   */
  inline fun <reified T : Any> getOrNull(): T? {
    return getOrNull(T::class)
  }

  /**
   * オブジェクトの @Inject アノテーションが付けられたフィールドにサービスを注入します。
   *
   * @param target 注入対象のオブジェクト
   */
  fun inject(target: Any) {
    val kClass = target::class
    kClass.declaredMemberProperties.forEach { prop ->
      if (prop.findAnnotation<Inject>() != null && prop is KMutableProperty<*>) {
        val serviceClass = prop.returnType.classifier as? KClass<*>
        if (serviceClass != null) {
          val service = getOrNull(serviceClass)
          if (service != null) {
            prop.isAccessible = true
            prop.setter.call(target, service)
          }
        }
      }
    }
  }

  /**
   * 登録されているすべての [Service] を実行します。
   */
  fun runServices() {
    services.values.filterIsInstance<Service>().forEach { it.run() }
  }

  /**
   * 登録されているすべての [ThrowableService] を実行します。
   *
   * @throws Throwable サービス実行中に発生した最初の例外
   */
  fun runThrowableServices() {
    services.values.filterIsInstance<ThrowableService>().forEach { it.run() }
  }

  /**
   * サービスを遅延注入するためのデリゲートを作成します。
   *
   * @return 注入されたサービスを返すデリゲート
   */
  inline fun <reified T : Any> inject(): ReadOnlyProperty<Any?, T> {
    return ReadOnlyProperty { _, _ -> get(T::class) }
  }

  /**
   * サービスを遅延注入するためのデリゲートを作成します。登録されていない場合は null を返します。
   *
   * @return 注入されたサービス、または null を返すデリゲート
   */
  inline fun <reified T : Any> injectOrNull(): ReadOnlyProperty<Any?, T?> {
    return ReadOnlyProperty { _, _ -> getOrNull(T::class) }
  }

  /**
   * 指定されたクラスのインスタンスを取得します。
   * まだ登録されていない場合は、インスタンスを作成して登録します。
   *
   * @param clazz 取得するクラス
   * @return インスタンス
   */
  @Suppress("UNCHECKED_CAST")
  fun <T : Any> getOrCreate(clazz: KClass<T>): T {
    val existing = getOrNull(clazz)
    if (existing != null) return existing

    val implementationClass = (bindings[clazz] ?: clazz) as KClass<out T>
    val instance = createInstance(implementationClass)
    register(clazz, instance)
    return instance
  }

  /**
   * 指定されたクラスのインスタンスを取得します。
   * まだ登録されていない場合は、インスタンスを作成して登録します。
   */
  inline fun <reified T : Any> getOrCreate(): T {
    return getOrCreate(T::class)
  }

  /**
   * 指定されたクラスの新しいインスタンスを作成し、依存関係を解決します。
   *
   * @param clazz 作成するクラス
   * @return 作成されたインスタンス
   */
  fun <T : Any> createInstance(clazz: KClass<T>): T {
    val constructor = clazz.primaryConstructor ?: clazz.constructors.firstOrNull()
      ?: throw IllegalArgumentException("No constructor found for ${clazz.qualifiedName}")

    val args = constructor.parameters.associateWith { param ->
      val paramClass = param.type.classifier as? KClass<*>
        ?: throw IllegalArgumentException("Unsupported parameter type: ${param.type}")
      if (param.isOptional && getOrNull(paramClass) == null) {
        null
      } else {
        getOrCreate(paramClass)
      }
    }.filter { it.value != null || !it.key.isOptional }

    val instance = constructor.callBy(args)
    inject(instance)
    return instance
  }

  /**
   * すべての登録済みサービスをクリアします。
   */
  fun clear() {
    services.clear()
    bindings.clear()
  }
}
