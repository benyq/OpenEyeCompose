package com.benyq.compose.open.eye.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.benyq.compose.open.eye.R
import com.benyq.compose.open.eye.nav.Destinations
import com.benyq.compose.open.eye.nav.LocalNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen() {
    val coroutineScope = rememberCoroutineScope()

    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(1200)
            navController.navigate(Destinations.Main.path) {
                popUpTo(Destinations.Splash.path) {
                    inclusive = true
                }
            }
        }
    }
    Image(
        painter = painterResource(id = R.drawable.home_launch_screen),
        contentDescription = "开屏页面",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}