package com.example.powerfit.view.Student

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun YouTubeVideoPlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val videoId = extractYoutubeVideoId(videoUrl)
    val embedUrl = "https://www.youtube.com/embed/$videoId"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                loadUrl(embedUrl)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
}

// Função utilitária para extrair o ID do vídeo do YouTube
fun extractYoutubeVideoId(url: String): String {
    return url.substringAfter("v=").substringBefore("&")
}
