package com.benyq.compose.open.eye.business.video

import androidx.lifecycle.viewModelScope
import com.benyq.compose.open.eye.base.common.BaseViewModel
import com.benyq.compose.open.eye.base.common.L
import com.benyq.compose.open.eye.base.common.mvi.UiEvent
import com.benyq.compose.open.eye.model.ItemData
import com.benyq.compose.open.eye.scene.SceneEvent
import com.benyq.compose.open.eye.scene.source.VideoItem
import com.benyq.tikbili.player.dispather.Event
import com.benyq.tikbili.player.dispather.EventDispatcher
import com.benyq.tikbili.player.playback.PlaybackController
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val playbackController = PlaybackController().apply {
        addPlaybackListener(object : EventDispatcher.EventListener {
            override fun onEvent(event: Event) {
                when (event.code) {
                    SceneEvent.Action.MORE_ACTION -> {
                        viewModelScope.launch {
                            _eventFlow.emit(VideoEvent.MoreActionEvent)
                        }
                    }

                    SceneEvent.Action.FULLSCREEN -> {
                        viewModelScope.launch {
                            _eventFlow.emit(VideoEvent.FullscreenEvent)
                        }
                    }

                    SceneEvent.Action.BACK -> {
                        viewModelScope.launch {
                            _eventFlow.emit(VideoEvent.BackActionEvent)
                        }
                    }
                }
            }
        })
    }

    private val _videoParam = MutableStateFlow(VideoItem.empty)

    val videoParam = _videoParam.asStateFlow()

    fun createVideoParams(data: ItemData) {
        val playInfo = data.playInfo?.find { it.type == "high" }
        _videoParam.value = VideoItem(
            id = data.id.toString(),
            videoUrl = data.playUrl,
            title = data.title,
            videoWidth = playInfo?.width ?: 0,
            videoHeight = playInfo?.height ?: 0,
            duration = data.duration * 1000L,
            coverUrl = data.cover.feed,
            byteSize = playInfo?.urlList?.first()?.size ?: 0L,
        ).apply {

        }
    }
}

sealed class VideoEvent : UiEvent {
    data object FullscreenEvent : VideoEvent()
    data object MoreActionEvent : VideoEvent()
    data object BackActionEvent : VideoEvent()
}