package com.peco2222.kitsune.encrypt

interface SecureCrypto {
  fun encrypt(input: String): String
  fun decrypt(input: String): String
}
