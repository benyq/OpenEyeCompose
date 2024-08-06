package com.benyq.compose.open.eye

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.benyq.compose.open.eye.nav.NavGraph
import com.benyq.compose.open.eye.ui.theme.OpenEyeComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenEyeComposeTheme {
                NavGraph()
            }
        }
    }
}
