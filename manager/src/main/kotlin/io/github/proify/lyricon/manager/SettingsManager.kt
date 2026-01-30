package io.github.proify.lyricon.manager

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("manager_settings", Context.MODE_PRIVATE)

    var apiUrl: String
        get() = prefs.getString("api_url", "http://127.0.0.1:3000") ?: "http://127.0.0.1:3000"
        set(value) = prefs.edit().putString("api_url", value).apply()

    var lyricMode: String
        get() = prefs.getString("lyric_mode", "both") ?: "both"
        set(value) = prefs.edit().putString("lyric_mode", value).apply()

    var cookie: String?
        get() = prefs.getString("cookie", null)
        set(value) = prefs.edit().putString("cookie", value).apply()
}
