package com.example.ar_ka_rodo_kine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
                                config =
                                    json.decodeFromString<Config>(inputStream.bufferedReader().use { it.readText() })
                            } finally {
                                urlConnection.disconnect()
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(
                            modifier = Modifier.align(Alignment.Center),
                            enter = fadeIn() + scaleIn(),
                            visible = config != null
                        ) {
                            val config = config!!
                            if (config.enabled) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(69.dp),
                                        fontSize = 69.sp,
                                        text = "TAIP!",
                                    )
                                    val lazyListState = rememberLazyListState()
                                    LazyColumn(
                                        state = lazyListState,
                                        contentPadding = PaddingValues((69 / 3f).dp)
                                    ) {
                                        items(config.goodMovieList) { movie ->
                                            ElevatedCard(
                                                modifier = Modifier.padding(vertical = 6.9.dp),
                                                onClick = {
                                                val browserIntent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(movie.url)
                                                )
                                                startActivity(browserIntent)
                                            }) {
                                                Text(
                                                    modifier = Modifier.padding((6.9 * 6.9 - 6.9 - 6 - 9).dp),
                                                    text = movie.url
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(69.dp),
                                    fontSize = 69.sp,
                                    text = "NE...",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Serializable
data class Config(
    @SerialName("enabled") val enabled: Boolean,
    @SerialName("goodMovieList") val goodMovieList: List<Movie>
)

@Serializable
data class Movie(
    @SerialName("url") val url: String,
    @SerialName("imageUrl") val imageUrl: String
)
