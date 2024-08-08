package com.benyq.compose.open.eye.business.video

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benyq.compose.open.eye.base.common.L
import com.benyq.compose.open.eye.base.common.fullScreen
import com.benyq.compose.open.eye.nav.LocalNavController
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeConfig

@Composable
fun VideoFullScreen(viewModel: VideoPlayerViewModel = viewModel()) {

    val navController = LocalNavController.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            viewModel.eventFlow.collect {
                when (it) {
                    is VideoEvent.BackActionEvent -> {
                        finishScreen(context, navController)
                    }
                }
            }
        }
    }

    BackHandler {
        finishScreen(context, navController)
    }

    LaunchedEffect(Unit) {
        (context as ComponentActivity).fullScreen()
        enterFullscreen(context)
    }

    val videoParam by viewModel.videoParam.collectAsState()
    Box {
        VideoPlayer(param = videoParam,
            playbackController = viewModel.playbackController,
            fullscreen = true
        )
    }
}

private fun finishScreen(context: Context, navController: NavController) {
    (context as ComponentActivity).fullScreen(false)
    exitFullscreen(context)
    navController.navigateUp()
}


private fun enterFullscreen(context: Context) {
    (context as ComponentActivity).requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    AutoSizeConfig.getInstance()
        .setDesignWidthInDp(640)
}

private fun exitFullscreen(context: Context) {
    (context as ComponentActivity).requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    AutoSizeConfig.getInstance()
        .setDesignWidthInDp(375)
}