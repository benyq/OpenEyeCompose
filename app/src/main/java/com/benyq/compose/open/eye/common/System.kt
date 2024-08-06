package com.benyq.compose.open.eye.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetAppearanceStatusBar(darkIcons: Boolean) {
    val activity = LocalContext.current as Activity
    val window = activity.window
    val view = LocalView.current

    SideEffect {
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = darkIcons
    }
}


internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Picture in picture should be called in the context of an Activity")
}


fun Context.getNetworkType(): NetworkType {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.NONE
    }
}


enum class NetworkType {
    WIFI, CELLULAR, NONE
}