package com.peco2222.kitsune.encrypt

import javax.crypto.Cipher

object Ciphers {
  val AES_CBC_NOPADDING = create("AES/CBC/NoPadding")
  val AES_CBC_PKCS5PADDING = create("AES/CBC/PKCS5Padding")
  val AES_ECB_NOPADDING = create("AES/ECB/NoPadding")
  val AES_ECB_PKCS5PADDING = create("AES/ECB/PKCS5Padding")
  val DES_CBC_NOPADDING = create("DES/CBC/NoPadding")
  val DES_CBC_PKCS5PADDING = create("DES/CBC/PKCS5Padding")
  val DES_ECB_NOPADDING = create("DES/ECB/NoPadding")
  val DES_ECB_PKCS5PADDING = create("DES/ECB/PKCS5Padding")
  val DESede_CBC_NOPADDING = create("DESede/CBC/NoPadding")
  val DESede_CBC_PKCS5PADDING = create("DESede/CBC/PKCS5Padding")
  val DESede_ECB_NOPADDING = create("DESede/ECB/NoPadding")
  val DESede_ECB_PKCS5PADDING = create("DESede/ECB/PKCS5Padding")
  val RSA_ECB_PKCS1PADDING = create("RSA/ECB/PKCS1Padding")
  val RSA_ECB_OAEPWITHSHA_1ANDMGF1PADDING = create("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
  val RSA_ECB_OAEPWITHSHA_256ANDMGF1PADDING = create("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")

  private fun create(name: String): Cipher = Cipher.getInstance(name)
}
