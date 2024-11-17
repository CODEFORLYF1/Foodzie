package com.example.appmain.ui.theme

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.platform.LocalContext

@Composable
fun VideoPlayerScreen(videoUri: String) {
    val context = LocalContext.current
    val exoPlayer = SimpleExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
        setMediaItem(mediaItem)
        prepare()
    }

    AndroidView(factory = { context ->
        PlayerView(context).apply {
            player = exoPlayer
        }
    })
}
