package io.github.proify.lyricon.manager

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var settingsManager: SettingsManager
    private var qrCheckJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsManager = SettingsManager(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ManagerScreen(settingsManager)
                }
            }
        }
    }

    @Composable
    fun ManagerScreen(settingsManager: SettingsManager) {
        var apiUrl by remember { mutableStateOf(settingsManager.apiUrl) }
        var lyricMode by remember { mutableStateOf(settingsManager.lyricMode) }
        var cookie by remember { mutableStateOf(settingsManager.cookie) }

        var qrBase64 by remember { mutableStateOf<String?>(null) }
        var qrStatus by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lyricon Manager", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = apiUrl,
                onValueChange = {
                    apiUrl = it
                    settingsManager.apiUrl = it
                },
                label = { Text("Netease API Base URL") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = {
                        scope.launch {
                            val repo = NeteaseApiRepository(apiUrl)
                            qrStatus = if (repo.ping()) "Connection Successful" else "Connection Failed"
                        }
                    }) {
                        Text("Test")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lyric Display Mode", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = lyricMode == "original", onClick = {
                    lyricMode = "original"
                    settingsManager.lyricMode = "original"
                })
                Text("Original")
                RadioButton(selected = lyricMode == "translation", onClick = {
                    lyricMode = "translation"
                    settingsManager.lyricMode = "translation"
                })
                Text("Translation")
                RadioButton(selected = lyricMode == "both", onClick = {
                    lyricMode = "both"
                    settingsManager.lyricMode = "both"
                })
                Text("Both")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (cookie != null) {
                Text("Status: Logged In", color = MaterialTheme.colorScheme.primary)
                Button(onClick = {
                    settingsManager.cookie = null
                    cookie = null
                }) {
                    Text("Logout")
                }
            } else {
                Text("Status: Not Logged In")
                Button(onClick = {
                    scope.launch {
                        val repo = NeteaseApiRepository(apiUrl)
                        val key = repo.getQrKey() ?: return@launch
                        val qr = repo.getQrCode(key) ?: return@launch
                        qrBase64 = qr
                        qrStatus = "Waiting for scan..."

                        qrCheckJob?.cancel()
                        qrCheckJob = lifecycleScope.launch {
                            while (true) {
                                delay(3000)
                                val (code, newCookie) = repo.checkQrStatus(key)
                                when (code) {
                                    800 -> {
                                        qrStatus = "QR Code expired"
                                        break
                                    }
                                    801 -> qrStatus = "Waiting for scan..."
                                    802 -> qrStatus = "Waiting for confirmation..."
                                    803 -> {
                                        qrStatus = "Success!"
                                        settingsManager.cookie = newCookie
                                        cookie = newCookie
                                        qrBase64 = null
                                        break
                                    }
                                }
                            }
                        }
                    }
                }) {
                    Text("Login via QR Code")
                }
            }

            qrStatus.takeIf { it.isNotEmpty() }?.let {
                Text(it, modifier = Modifier.padding(8.dp))
            }

            qrBase64?.let { base64 ->
                val pureBase64 = if (base64.contains(",")) base64.split(",")[1] else base64
                val imageBytes = Base64.decode(pureBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}
