package com.benyq.compose.open.eye.scene.source

import com.benyq.tikbili.player.source.MediaSource
import java.io.Serializable

/**
 *
 * @author benyq
 * @date 4/17/2024
 *
 */
data class VideoItem(
    val id: String,
    val title: String,
    val coverUrl: String,
    val videoUrl: String,
    val videoWidth: Int,
    val videoHeight: Int,
    val duration: Long,
    val byteSize: Long,
): Serializable {

    var mediaSource: MediaSource? = null
        private set

    companion object {
        private const val EXTRA_VIDEO_ITEM = "extra_video_item"
        fun get(mediaSource: MediaSource): VideoItem? {
            return mediaSource.getExtra(EXTRA_VIDEO_ITEM, VideoItem::class.java)
        }

        fun set(mediaSource: MediaSource?, videoItem: VideoItem) {
            mediaSource ?: return
            mediaSource.putExtra(EXTRA_VIDEO_ITEM, videoItem)
        }

        fun toMediaSource(videoItem: VideoItem): MediaSource {
            val mediaSource = videoItem.mediaSource ?: createMediaSource(videoItem).apply {
                videoItem.mediaSource = this
            }
            set(mediaSource, videoItem)
            return mediaSource
        }

        private fun createMediaSource(videoItem: VideoItem): MediaSource {
            return MediaSource(videoItem.id, videoItem.videoUrl, videoItem.coverUrl, videoItem.videoWidth, videoItem.videoHeight, videoItem.duration, videoItem.byteSize)
        }

        val empty = VideoItem(
            id = "",
            title = "",
            coverUrl = "",
            videoUrl = "",
            videoWidth = 0,
            videoHeight = 0,
            duration = 0L,
            byteSize = 0L
        )
    }
}