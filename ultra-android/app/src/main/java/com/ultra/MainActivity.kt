package com.ultra

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class MainActivity :
    AppCompatActivity() {
    private val apiClient = ApiClient()
    private lateinit var output: TextView
    private lateinit var input: EditText
    private lateinit var serverUrl: EditText
    private lateinit var sendButton: Button

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_main
        )
        output = findViewById(R.id.output)
        input = findViewById(R.id.messageInput)
        serverUrl = findViewById(R.id.serverUrl)
        sendButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val message = input.text.toString().trim()
        val url = serverUrl.text.toString().trim()
        if (message.isEmpty() || url.isEmpty()) return
        input.text.clear()
        sendButton.isEnabled = false
        output.append("\nYou: $message\n")
        thread {
            try {
                val answer = apiClient.sendMessage(url, message)
                runOnUiThread { output.append("ULTRA: $answer\n") }
            } catch (error: Exception) {
                runOnUiThread { output.append("Error: ${error.message}\n") }
            } finally {
                runOnUiThread { sendButton.isEnabled = true }
            }
        }
    }
}