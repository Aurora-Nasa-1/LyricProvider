package io.github.proify.lyricon.manager

import android.content.Context
import android.media.MediaMetadata
import android.media.session.PlaybackState
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.*
import com.highcapable.yukihookapi.hook.log.YLog
import io.github.proify.lyricon.manager.parser.LyricParser
import io.github.proify.lyricon.lyric.model.RichLyricLine
import io.github.proify.lyricon.lyric.model.LyricWord
import io.github.proify.lyricon.lyric.model.Song
import io.github.proify.lyricon.provider.LyriconProvider
import io.github.proify.lyricon.provider.ProviderLogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ManagerConstants {
    const val ICON = "<svg viewBox=\"0 0 24 24\"><path fill=\"currentColor\" d=\"M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10,10-4.48,10-10S17.52,2,12,2zm0,14.5c-2.49,0-4.5-2.01-4.5-4.5S9.51,7.5,12,7.5,16.5,9.51,16.5,12,14.49,16.5,12,16.5z\"/></svg>"
}

object GlobalHook : YukiBaseHooker() {
    private var provider: LyriconProvider? = null
    private var lastTitle: String? = null
    private var lastArtist: String? = null
    private var loadingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onHook() {
        // Skip some common non-music apps
        if (packageName == "android" || packageName == "com.android.systemui" || packageName == "com.android.settings") return

        "android.media.session.MediaSession".toClass().method {
            name = "setMetadata"
            param("android.media.MediaMetadata")
        }.hook {
            after {
                val metadata = args[0] as? MediaMetadata ?: return@after
                val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE) ?: return@after
                val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: ""
                val duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION)

                onMetadataChanged(appContext ?: return@after, title, artist, duration)
            }
        }

        "android.media.session.MediaSession".toClass().method {
            name = "setPlaybackState"
            param("android.media.session.PlaybackState")
        }.hook {
            after {
                val state = args[0] as? PlaybackState ?: return@after
                val isPlaying = state.state == PlaybackState.STATE_PLAYING
                val position = state.position

                provider?.player?.apply {
                    setPlaybackState(isPlaying)
                    setPosition(position)
                }
            }
        }
    }

    private fun onMetadataChanged(context: Context, title: String, artist: String, duration: Long) {
        if (title == lastTitle && artist == lastArtist) return
        lastTitle = title
        lastArtist = artist

        YLog.debug("Metadata changed: $title - $artist")

        if (provider == null) {
            provider = LyriconProvider(
                context = context,
                providerPackageName = "io.github.proify.lyricon.manager",
                playerPackageName = context.packageName,
                logo = ProviderLogo.fromSvg(ManagerConstants.ICON)
            ).apply {
                register()
            }
        }

        loadingJob?.cancel()
        loadingJob = scope.launch {
            val prefs = context.prefs("manager_settings")
            val apiUrl = prefs.getString("api_url", "http://127.0.0.1:3000")
            val cookie = prefs.getString("cookie", "")
            val lyricMode = prefs.getString("lyric_mode", "both")

            val lyricService = LyricService(apiUrl, if (cookie.isEmpty()) null else cookie)
            val songId = lyricService.searchSong(title, artist)

            val song = Song().apply {
                this.id = songId?.toString() ?: "${title}_${artist}"
                this.name = title
                this.artist = artist
                this.duration = duration
            }

            if (songId != null) {
                val lyricData = lyricService.getLyrics(songId)
                if (lyricData != null) {
                    val response = io.github.proify.lyricon.manager.parser.model.LyricResponse(
                        lrc = lyricData.lrc,
                        lrcTranslateLyric = lyricData.tlrc,
                        yrc = lyricData.yrc,
                        yrcTranslateLyric = lyricData.ytlrc,
                        musicId = songId
                    )
                    val info = LyricParser.toLyricInfo(response)
                    song.lyrics = info.lyrics.map { line ->
                        RichLyricLine(
                            begin = line.start,
                            end = line.end,
                            duration = line.duration,
                            text = if (lyricMode == "translation") (line.translation ?: "") else (line.text ?: ""),
                            translation = if (lyricMode == "both") (line.translation ?: "") else "",
                            words = if (lyricMode != "translation") {
                                line.words.map { word ->
                                    LyricWord(
                                        begin = word.start,
                                        end = word.end,
                                        duration = word.duration,
                                        text = word.text
                                    )
                                }
                            } else emptyList()
                        )
                    }
                }
            }

            provider?.player?.setSong(song)
        }
    }
}
