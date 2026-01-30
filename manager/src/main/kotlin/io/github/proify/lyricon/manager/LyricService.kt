package io.github.proify.lyricon.manager

import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder

class LyricService(private val baseUrl: String, private val cookie: String?) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    data class LyricData(
        val lrc: String? = null,
        val tlrc: String? = null,
        val yrc: String? = null,
        val ytlrc: String? = null
    )

    suspend fun searchSong(title: String, artist: String): Long? {
        val keywords = URLEncoder.encode("$title $artist", "UTF-8")
        val url = "$baseUrl/search?keywords=$keywords&limit=1&type=1"
        val request = Request.Builder().url(url).apply {
            if (cookie != null) header("Cookie", cookie)
        }.build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val result = json.parseToJsonElement(body).jsonObject["result"]?.jsonObject
                val songs = result?.get("songs")?.jsonArray
                if (songs != null && songs.isNotEmpty()) {
                    songs[0].jsonObject["id"]?.jsonPrimitive?.content?.toLongOrNull()
                } else null
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getLyrics(songId: Long): LyricData? {
        val url = "$baseUrl/lyric?id=$songId"
        val request = Request.Builder().url(url).apply {
            if (cookie != null) header("Cookie", cookie)
        }.build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val obj = json.parseToJsonElement(body).jsonObject
                val lrc = obj["lrc"]?.jsonObject?.get("lyric")?.jsonPrimitive?.content
                val tlrc = obj["tlyric"]?.jsonObject?.get("lyric")?.jsonPrimitive?.content
                val yrc = obj["yrc"]?.jsonObject?.get("lyric")?.jsonPrimitive?.content
                val ytlrc = obj["ytlrc"]?.jsonObject?.get("lyric")?.jsonPrimitive?.content
                LyricData(lrc, tlrc, yrc, ytlrc)
            }
        } catch (e: IOException) {
            null
        }
    }
}
