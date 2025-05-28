package com.example.powerfit.view.Student

import android.util.Log
import android.webkit.WebChromeClient
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
            Log.d("YouTubePlayer", "Criando WebView para $embedUrl")
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient() // Ajuda na reprodução de vídeo
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                settings.userAgentString = WebSettings.getDefaultUserAgent(context)
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
    return if (url.contains("youtu.be/")) url.substringAfter("youtu.be/").substringBefore("?") else url.substringAfter("v=").substringBefore("&")
}
