package com.example.dataencryptiondecryption.ext

import android.util.Base64
import com.example.dataencryptiondecryption.CryptoManager

fun String.encrypt(): String {
    val encryptedBytes = CryptoManager().encrypt(this.toByteArray())
    return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
}

fun String.decrypt(): String? {
    val decodedBytes = Base64.decode(this, Base64.NO_WRAP)
    val decryptedBytes = CryptoManager().decrypt(decodedBytes)
    return decryptedBytes?.toString(Charsets.UTF_8)
}
