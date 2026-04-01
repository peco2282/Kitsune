package com.peco2222.kitsune.encrypt.jvm

import com.peco2222.kitsune.encrypt.SecureCrypto

class AesCrypt: SecureCrypto {
  override fun encrypt(input: String): String = input

  override fun decrypt(input: String): String = input
}
