package com.ultra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var question by remember {
                mutableStateOf("")
            }

            Column {

                Text("ULTRA")

                TextField(
                    value = question,
                    onValueChange = { question = it }
                )

                Button(
                    onClick = {
                        // Send to Ultra Backend
                    }
                ) {
                    Text("Ask ULTRA")
                }
            }
        }
    }
}