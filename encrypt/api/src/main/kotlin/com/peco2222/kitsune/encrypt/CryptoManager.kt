package com.peco2222.kitsune.encrypt

class CryptoManager {
    companion object {
        init {
            // DLLがある場所を指定。テスト時は絶対パスで書くのが確実です。
            // プロジェクトのルートにあるなら "./mycrypto.dll"
            System.load("C:\\Users\\admin\\Project\\Kitsune\\encrypt\\api\\src\\main\\cpp\\mycrypto.dll")
        }
    }
    /**
     * C言語で実装されたネイティブメソッド
     */
    external fun encryptNative(data: ByteArray, key: ByteArray): ByteArray?

    fun encrypt(data: String, key: String): ByteArray? {
        val dataBytes = data.toByteArray(Charsets.UTF_8)
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        return encryptNative(dataBytes, keyBytes)
    }
}

fun main() {
    val manager = CryptoManager()
    val secret = "Hello Native C!"
    val key = "pass123"

    val encrypted = manager.encrypt(secret, key)

    println("Original: $secret")
    println("Encrypted (Hex): ${encrypted?.joinToString("") { "%02x".format(it) }}")
}
