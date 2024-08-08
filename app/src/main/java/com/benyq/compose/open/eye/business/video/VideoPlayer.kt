package com.benyq.compose.open.eye.business.video

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.benyq.compose.open.eye.base.common.L
import com.benyq.compose.open.eye.scene.PlayScene
import com.benyq.compose.open.eye.scene.layer.BrightnessVolumeLayer
import com.benyq.compose.open.eye.scene.layer.GestureLayer
import com.benyq.compose.open.eye.scene.layer.PlayPauseLayer
import com.benyq.compose.open.eye.scene.layer.SpeedSelectDialogLayer
import com.benyq.compose.open.eye.scene.layer.TimeProgressBarLayer
import com.benyq.compose.open.eye.scene.layer.TitleBarLayer
import com.benyq.tikbili.player.helper.DisplayModeHelper
import com.benyq.tikbili.player.playback.PlaybackController
import com.benyq.tikbili.player.playback.VideoLayerHost
import com.benyq.tikbili.player.playback.VideoView
import com.benyq.tikbili.player.source.MediaSource

data class VideoParams(
    val tag: String,
    val url: String,
    val cover: String,
    val width: Int, val height: Int,
    val duration: Long,
    val byteSize: Long,
) {
    companion object {
        val empty = VideoParams(
            tag = "",
            url = "",
            cover = "",
            width = 0,
            height = 0,
            duration = 0L,
            byteSize = 0L
        )
    }
}

@Composable
fun VideoPlayer(
    param: VideoParams,
    playbackController: PlaybackController,
    modifier: Modifier = Modifier,
    fullscreen: Boolean = false,
    playWhenReady: Boolean = true
) {

    DisposableEffect(param) {
        playbackController.videoView()?.bindDataSource(MediaSource(
            param.tag,
            param.url,
            param.cover,
            param.width, param.height, param.duration, param.byteSize
        ))
        playbackController.startPlayback(playWhenReady)
        onDispose {
            playbackController.stopPlayback()
        }
    }


    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(factory = {
            val videoView = VideoView(it)
            videoView.layoutParams =
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams
                        .MATCH_PARENT,
                    ViewGroup.LayoutParams
                        .MATCH_PARENT
                )
            val layerHost = VideoLayerHost(it)
            layerHost.addLayer(GestureLayer())
            layerHost.addLayer(TimeProgressBarLayer())
            layerHost.addLayer(TitleBarLayer())
            layerHost.addLayer(PlayPauseLayer())
            layerHost.addLayer(SpeedSelectDialogLayer())
            layerHost.addLayer(BrightnessVolumeLayer())
            layerHost.attachToVideoView(videoView)
            videoView.setPlayScene(if (fullscreen) PlayScene.SCENE_FULLSCREEN else PlayScene.SCENE_DETAIL)
            playbackController.bind(videoView)
            videoView.setDisplayMode(DisplayModeHelper.DISPLAY_MODE_ASPECT_FILL_Y)
            videoView.setupDisplayView()
            videoView
        })
    }
}

