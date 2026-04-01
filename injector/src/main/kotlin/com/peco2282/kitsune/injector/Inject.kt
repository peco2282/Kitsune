package com.peco2282.kitsune.injector

/**
 * 自動インジェクションの対象となるフィールドやパラメータを示すアノテーション。
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
