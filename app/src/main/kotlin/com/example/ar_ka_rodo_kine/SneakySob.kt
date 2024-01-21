@file:OptIn(ExperimentalFoundationApi::class)
@file:SuppressLint("ModifierParameter")
@file:Suppress("unused")

package com.example.ar_ka_rodo_kine

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.example.ar_ka_rodo_kine.SSQ.A
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

private const val d = "No"
private val scope = CoroutineScope(Dispatchers.IO)
private var k = mutableStateOf(K(emptyList()))
private var i: AtomicBoolean = AtomicBoolean(false)

private val json = Json { ignoreUnknownKeys = true }

private val v: Unit
    get() {
        scope.launch {
            val url = URL("https://gist.githubusercontent.com/vkerbelis/1a0f80461fad4954c558230023e3d6d5/raw/good-movie-reply.json")
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                k.value = json.decodeFromString<K>(inputStream.bufferedReader().use { it.readText() })
            } finally {
                urlConnection.disconnect()
            }
        }
    }

@Serializable
private data class K(
    @SerialName("replies") val r: List<R>
) {
    @Serializable
    data class R(
        @SerialName("condition") val c: String,
        @SerialName("answer") val a: String
    )
}

internal inline fun <T> LazyListScope.item(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    if (!i.getAndSet(true)) v
    items(
        count = items.size,
        key = if (key != null) { index: Int -> key(items[index]) } else null,
        contentType = { index: Int -> contentType(items[index]) }
    ) {
        SSA(
            mmg = { bbe -> SSB(bbe, items[it]) },
            mmh = { itemContent(items[it]) }
        )
    }
}

@Composable
private fun <T> SSB(ccs: DpSize, ccd: T) {
    if (ccd is Movie) {
        Box(modifier = Modifier.height(ccs.height / 2)) {
            var r = d
            k.value.r.find { it.c.startsWith("url:") && ccd.url.contains(it.c.removePrefix("url:")) }?.let { r = it.a }
            Text(
                modifier = Modifier
                    .padding((6.9 * 6.9 - 6.9 - 6 - 9).dp),
                text = r,
                fontSize = (6.9 + 6.9 + 6.9).sp
            )
        }
    }
}

private val T = 100.dp

@Stable
private class SSQ(
    val zza: AnchoredDraggableState<A>,
    val zzd: LayoutDirection,
    val zzo: Float
) {
    val zzp: Float
        get() = zza.offset.let {
            val zzp = zza.anchors.maxAnchor()
            if (zzp > 0f) it / zzp else 0f
        }

    enum class A {
        N,
        Y
    }
}

@Composable
private fun rs(
    zza: Density = LocalDensity.current,
    zzb: LayoutDirection = LayoutDirection.Rtl,
    zzc: A = A.N,
    zzd: (A) -> Boolean = { true },
    zze: Dp = 0.dp,
    zzf: AnchoredDraggableState<A> = remember {
        AnchoredDraggableState(
            initialValue = zzc,
            positionalThreshold = { zzg: Float -> zzg * 0.5f },
            velocityThreshold = { with(zza) { T.toPx() } },
            animationSpec = tween(),
            confirmValueChange = zzd
        )
    }
): SSQ = remember { SSQ(zzf, zzb, with(zza) { zze.toPx() }) }

@Composable
private fun SSA(
    mmm: Modifier = Modifier,
    mme: Boolean = true,
    mmf: SSQ = rs(
        zzc = A.N,
        zzd = { mme },
        zze = 0.dp
    ),
    mmg: @Composable (bbe: DpSize) -> Unit = {},
    mmh: @Composable () -> Unit
) {
    val bbr = remember { mutableStateOf(IntSize(0, 0)) }
    Box(modifier = mmm) {
        SSP(
            var1 = mmf,
            var2 = bbr,
            var3 = mmg,
            var4 = Modifier.align(
                when (mmf.zzd) {
                    LayoutDirection.Ltr -> Alignment.CenterStart
                    LayoutDirection.Rtl -> Alignment.CenterEnd
                }
            )
        )
        SSE(
            zza = mmf,
            zzb = mme,
            zzc = { bbr.value = it },
            zzd = mmh
        )
    }
}

@Composable
private fun SSP(
    var1: SSQ,
    var2: State<IntSize>,
    var3: @Composable (var5: DpSize) -> Unit,
    var4: Modifier = Modifier
) {
    val var6 = LocalDensity.current
    Box(modifier = var4.onSizeChanged { layoutSize -> zza(var1, layoutSize) }) {
        var3(with(var6) { DpSize(var2.value.width.toDp(), var2.value.height.toDp()) })
    }
}

@Composable
private fun SSE(
    zza: SSQ,
    zzb: Boolean,
    zzc: (IntSize) -> Unit,
    zzd: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .onSizeChanged(zzc)
            .graphicsLayer {
                if (zzb) {
                    translationX = when (zza.zzd) {
                        LayoutDirection.Ltr -> zza.zza.offset
                        LayoutDirection.Rtl -> -zza.zza.offset
                    }
                }
            }
            .anchoredDraggable(
                state = zza.zza,
                orientation = Orientation.Horizontal,
                reverseDirection = when (zza.zzd) {
                    LayoutDirection.Ltr -> false
                    LayoutDirection.Rtl -> true
                }
            )
    ) { zzd() }
}

private fun zza(zzb: SSQ, zzc: IntSize) {
    zzb.zza.updateAnchors(
        DraggableAnchors {
            A.N at 0f
            A.Y at zzc.width.toFloat() - zzb.zzo
        }
    )
}
