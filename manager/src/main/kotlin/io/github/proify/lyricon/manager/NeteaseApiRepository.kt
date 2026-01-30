package io.github.proify.lyricon.manager

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NeteaseApiRepository(private val baseUrl: String) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun ping(): Boolean {
        val request = Request.Builder()
            .url("$baseUrl/ping")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful && response.body?.string()?.contains("pong") == true
            }
        } catch (e: IOException) {
            false
        }
    }

    suspend fun getQrKey(): String? {
        val request = Request.Builder()
            .url("$baseUrl/login/qr/key?timestamp=${System.currentTimeMillis()}")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                json.parseToJsonElement(body).jsonObject["data"]?.jsonObject?.get("unikey")?.jsonPrimitive?.content
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getQrCode(key: String): String? {
        val request = Request.Builder()
            .url("$baseUrl/login/qr/create?key=$key&qrimg=true&timestamp=${System.currentTimeMillis()}")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                json.parseToJsonElement(body).jsonObject["data"]?.jsonObject?.get("qrimg")?.jsonPrimitive?.content
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun checkQrStatus(key: String): Pair<Int, String?> {
        val request = Request.Builder()
            .url("$baseUrl/login/qr/check?key=$key&timestamp=${System.currentTimeMillis()}")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                val body = response.body?.string() ?: return 400 to null
                val obj = json.parseToJsonElement(body).jsonObject
                val code = obj["code"]?.jsonPrimitive?.content?.toIntOrNull() ?: 400
                val cookie = obj["cookie"]?.jsonPrimitive?.content
                code to cookie
            }
        } catch (e: IOException) {
            400 to null
        }
    }
}
