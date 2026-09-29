package com.ultra

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UltraApi(private val endpoint: String) {

    fun chat(message: String): String {
        var connection: HttpURLConnection? = null

        return try {
            connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 10_000
                readTimeout = 60_000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
            }

            val body = JSONObject().put("message", message).toString()
            connection.outputStream.use { output ->
                output.write(body.toByteArray(Charsets.UTF_8))
            }

            val responseStream = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = responseStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (connection.responseCode !in 200..299) {
                "ULTRA server error (${connection.responseCode})"
            } else {
                JSONObject(response).optString("response", "No response returned.")
            }
        } catch (error: IOException) {
            "Unable to reach ULTRA: ${error.message ?: "network error"}"
        } finally {
            connection?.disconnect()
        }
    }
}
