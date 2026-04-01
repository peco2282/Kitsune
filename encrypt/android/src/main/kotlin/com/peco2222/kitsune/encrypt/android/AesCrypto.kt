package com.peco2222.kitsune.encrypt.android

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.peco2222.kitsune.encrypt.Ciphers
import com.peco2222.kitsune.encrypt.SecureCrypto
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class AesCrypto(key: String, private val cipher: Cipher = Ciphers.AES_ECB_PKCS5PADDING) : SecureCrypto {
  private val keySpec = SecretKeySpec(key.toByteArray(), "AES")

  override fun encrypt(input: String): String {
    val gen = KeyGenerator.getInstance("AES")
    val spec =
      KeyGenParameterSpec.Builder("alias", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).apply {
        if (cipher.algorithm.contains("ECB")) setBlockModes(KeyProperties.BLOCK_MODE_ECB)
        else if (cipher.algorithm.contains("CBC")) setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        else if (cipher.algorithm.contains("CTR")) setBlockModes(KeyProperties.BLOCK_MODE_CTR)
        else if (cipher.algorithm.contains("GCM")) setBlockModes(KeyProperties.BLOCK_MODE_GCM)

        setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        setUserAuthenticationRequired(true)
      }.build()
    gen.init(spec)
    val key = gen.generateKey()
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    val encrypted = cipher.doFinal(input.toByteArray())
    return Base64.encodeToString(encrypted, Base64.DEFAULT)
  }

  override fun decrypt(input: String): String {
    cipher.init(Cipher.DECRYPT_MODE, keySpec)
    val decoded = Base64.decode(input, Base64.DEFAULT)
    return String(cipher.doFinal(decoded))
  }
}
