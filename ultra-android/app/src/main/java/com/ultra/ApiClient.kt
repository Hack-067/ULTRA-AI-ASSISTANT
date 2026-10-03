package com.ultra

import okhttp3.*

class ApiClient {

    private val client = OkHttpClient()

    fun sendMessage(
        msg: String
    ) {

        val body = RequestBody.create(
            MediaType.parse("application/json"),
            "{\"message\":\"$msg\"}"
        )

        val request = Request.Builder()
            .url("http://YOUR_SERVER_IP:5000/chat")
            .post(body)
            .build()

        client.newCall(request).execute()
    }
}