package com.example.dataencryptiondecryption

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.dataencryptiondecryption.ext.decrypt
import com.example.dataencryptiondecryption.ext.encrypt

@Composable
fun MainScreen() {

    var inputText by remember { mutableStateOf(TextFieldValue()) }
    var outputText by remember { mutableStateOf(TextFieldValue()) }

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
            label = { Text("Enter text to encrypt") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val encryptedText = inputText.text.encrypt()
                outputText = TextFieldValue(encryptedText)
            }
        ) {
            Text("Encrypt")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val decryptedText = outputText.text.decrypt()
                outputText = TextFieldValue(decryptedText ?: "")
            }
        ) {
            Text("Decrypt")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = outputText,
            onValueChange = { outputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Decrypted text") },
            enabled = false
        )
    }
}