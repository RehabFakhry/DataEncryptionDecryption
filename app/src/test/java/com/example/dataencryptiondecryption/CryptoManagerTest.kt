package com.example.dataencryptiondecryption

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

@ExtendWith(MockitoExtension::class)
class CryptoManagerTest {

    @Mock
    private lateinit var cipher: Cipher

    @Mock
    private lateinit var keyStore: KeyStore

    @Mock
    private lateinit var secretKey: SecretKey

    private lateinit var cryptoManager: CryptoManager

    @BeforeEach
    fun setup() {
        cryptoManager = CryptoManager(keyStore, cipher)
        `when`(keyStore.getEntry("secret", null))
            .thenReturn(KeyStore.SecretKeyEntry(secretKey))
    }

    @Test
    fun `given an existing key in keyStore then expected return this key`() {
        val key = cryptoManager.getKey()
        assertEquals(secretKey, key)
    }

    @Test
    fun `given an empty input bytes then expected return null`() {
        val invalidBytes = byteArrayOf()
        val decrypted = cryptoManager.decrypt(invalidBytes)
        assertNull(decrypted)
    }

    @Test
    fun `given a valid input bytes then expected return encrypted bytes with initial vector`() {
        val originalBytes = "This is the text to be Encrypted".toByteArray()
        val encryptedBytes = "EncryptedData".toByteArray()
        val iv = "Initial Vector".toByteArray()

        `when`(cipher.doFinal(originalBytes)).thenReturn(encryptedBytes)
        `when`(cipher.iv).thenReturn(iv)

        val encrypted = cryptoManager.encrypt(originalBytes)
        val ivAndEncryptedData = iv + encryptedBytes
        assertArrayEquals(ivAndEncryptedData, encrypted)
    }

    @Test
    fun `given a valid encrypted input bytes then expected return decrypted bytes equal to original bytes`() {
        val originalBytes = "This is the text to be Encrypted".toByteArray()
        val encryptedBytes = "EncryptedData".toByteArray()
        val iv = byteArrayOf()

        `when`(cipher.doFinal(originalBytes)).thenReturn(encryptedBytes)
        `when`(cipher.doFinal(encryptedBytes)).thenReturn(originalBytes)
        `when`(cipher.iv).thenReturn(iv)

        val encryptedData = cryptoManager.encrypt(originalBytes)
        val decryptedData = cryptoManager.decrypt(encryptedData)
        assertArrayEquals(decryptedData, originalBytes)
    }
}