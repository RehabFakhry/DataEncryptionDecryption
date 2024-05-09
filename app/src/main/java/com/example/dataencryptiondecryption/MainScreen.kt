package com.example.dataencryptiondecryption

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dataencryptiondecryption.ext.decrypt
import com.example.dataencryptiondecryption.ext.encrypt

@Composable
fun MainScreen() {

    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter text to encrypt", color = Color.Gray) }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val encryptedText = inputText.encrypt()
                outputText = encryptedText
            }
        ) {
            Text("Encrypt")
        }

        Button(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = {
                val decryptedText = outputText.decrypt()
                outputText = decryptedText ?: ""
            }
        ) {
            Text("Decrypt")
        }

        OutlinedTextField(
            value = outputText,
            onValueChange = { outputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Decrypted text") },
            enabled = false
        )
    }
}