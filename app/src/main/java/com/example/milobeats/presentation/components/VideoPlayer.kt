package com.example.milobeats.presentation.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    onPlayerReady: (YouTubePlayer) -> Unit = {}
) {
    val context = LocalContext.current

    val youTubePlayerView = remember {
        YouTubePlayerView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                    onPlayerReady(youTubePlayer)
                }
            })
        }
    }

    DisposableEffect(
        AndroidView(
            factory = { youTubePlayerView },
            modifier = modifier
                .fillMaxWidth()
                .height(240.dp)
        )
    ) {
        onDispose {
            youTubePlayerView.release()
        }
    }
}