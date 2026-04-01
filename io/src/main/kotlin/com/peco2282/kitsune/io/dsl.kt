package com.peco2282.kitsune.io

import java.io.File

/**
 * 文字列パスから FileChain を生成します。
 */
fun file(path: String) = FileChain(File(path))

/**
 * File オブジェクトから FileChain を生成します。
 */
fun file(file: File) = FileChain(file)

class FileChain(val file: File) {
    // 読み書き
    fun read(): String = file.readText()
    infix fun write(text: String) = apply { file.writeText(text) }
    infix fun append(text: String) = apply { file.appendText(text) }

    // 状態確認
    val exists: Boolean get() = file.exists()
    val isDirectory: Boolean get() = file.isDirectory
    val name: String get() = file.name

    // ディレクトリ・ファイル操作
    fun ensureExists() = apply {
        if (!exists) {
            if (file.name.contains(".")) file.createNewFile()
            else file.mkdirs()
        }
    }

    fun child(name: String) = FileChain(File(file, name))

    infix fun rename(newName: String): FileChain {
        val dest = File(file.parentFile, newName)
        file.renameTo(dest)
        return FileChain(dest)
    }

    fun delete() = file.deleteRecursively()

    // リスト操作
    fun listFiles() = file.listFiles()?.map { FileChain(it) } ?: emptyList()
    
    fun walk() = file.walk()
}
