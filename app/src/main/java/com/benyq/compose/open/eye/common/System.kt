package com.benyq.compose.open.eye.common

import android.app.Activity
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

