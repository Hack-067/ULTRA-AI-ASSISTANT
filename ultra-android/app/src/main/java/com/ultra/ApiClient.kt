package com.ultra

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    fun sendMessage(baseUrl: String, message: String): String {
        val payload = JSONObject().put("message", message).toString()
        val request = Request.Builder()
            .url("${baseUrl.trimEnd('/')}/chat")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IllegalStateException("Server error ${response.code}: $body")
            }
            return JSONObject(body).optString("response", "No response returned.")
        }
    }
}