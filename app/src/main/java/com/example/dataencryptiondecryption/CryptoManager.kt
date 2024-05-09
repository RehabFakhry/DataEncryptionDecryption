package com.example.dataencryptiondecryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * A class for managing encryption and decryption using Android KeyStore.
 *
 * This class provides methods for encrypting and decrypting byte arrays using a secret key stored
 * in the Android KeyStore. It utilizes the Cipher class with AES encryption in CBC mode and PKCS7 padding.
 *
 * @property keyStore The Android KeyStore instance used for key management (default: AndroidKeyStore)
 * @property encryptCipher The Cipher instance configured for encryption (transformation: $TRANSFORMATION)
 */
class CryptoManager(
    private val keyStore: KeyStore? = KeyStore.getInstance(ANDROID_KEYSTORE),
    private val encryptCipher: Cipher = Cipher.getInstance(TRANSFORMATION)
) {
    init {
        keyStore?.load(null)
    }

    /**
     * This method attempts to retrieve an existing secret key with the alias `KEY_ALIAS`.
     * If the key doesn't exist, a new key is generated using [createKey].
     *
     * @return The secret key from the key store.
     */
    fun getKey(): SecretKey {
        val existingKey = keyStore?.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    /** This method uses the [KeyGenerator] class to generate a new AES secret key with the following specifications:
     *  - Algorithm: [ALGORITHM] (AES)
     *  - Purpose: Encryption and decryption ([KeyProperties.PURPOSE_ENCRYPT] or [KeyProperties.PURPOSE_DECRYPT])
     *  - Block mode: [BLOCK_MODE] (CBC)
     *  - Padding: [PADDING] (PKCS7)
     *
     * The key is stored in the Android KeyStore with the alias `KEY_ALIAS`.
     *
     * @return The newly generated secret key.
     */
    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(BLOCK_MODE)
            setEncryptionPaddings(PADDING)
        }.build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * Encrypts the input byte array using AES encryption algorithm.
     *
     * @param bytes The byte array to be encrypted.
     * @return The encrypted byte array.
     */
    fun encrypt(bytes: ByteArray): ByteArray {
        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = encryptCipher.iv
        val encryptedBytes = encryptCipher.doFinal(bytes)
        return iv + encryptedBytes
    }

    /**
     * Decrypts the input byte array using AES decryption algorithm.
     *
     * @param bytes The byte array to be decrypted.
     * @return The decrypted byte array, or null if decryption fails.
     */
    fun decrypt(bytes: ByteArray): ByteArray? {
        return try {
            val iv = bytes.copyOfRange(0, encryptCipher.blockSize)
            val encryptedBytes = bytes.copyOfRange(encryptCipher.blockSize, bytes.size)
            encryptCipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
            encryptCipher.doFinal(encryptedBytes)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val KEY_ALIAS = "secret"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
