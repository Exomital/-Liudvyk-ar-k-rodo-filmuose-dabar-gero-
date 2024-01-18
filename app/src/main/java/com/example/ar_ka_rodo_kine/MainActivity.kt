package com.example.ar_ka_rodo_kine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ar_ka_rodo_kine.ui.theme.ArkarodokineTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream

import java.io.InputStream

import java.net.HttpURLConnection

import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArkarodokineTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var config: Config? by remember { mutableStateOf(null) }
                    LaunchedEffect(Unit) {
                        launch(Dispatchers.IO) {
                            val url =
                                URL("https://raw.githubusercontent.com/Exomital/-Liudvyk-ar-k-rodo-filmuose-dabar-gero-/master/config.json")
                            val urlConnection = url.openConnection() as HttpURLConnection
                            try {
                                val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                                config = Json.decodeFromString<Config>(inputStream.bufferedReader().use { it.readText() })
                            } finally {
                                urlConnection.disconnect()
                            }
                        }
                    }
                    config?.let { config ->
                        Greeting(config.enabled.toString())
                    }
                }
            }
        }
    }
}

@Serializable
data class Config(
    @SerialName("enabled") val enabled: Boolean
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArkarodokineTheme {
        Greeting("Android")
    }
}
