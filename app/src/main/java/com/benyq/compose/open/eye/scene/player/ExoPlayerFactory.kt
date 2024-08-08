package com.benyq.compose.open.eye.scene.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.benyq.tikbili.player.player.IPlayer
import com.benyq.tikbili.player.source.MediaSource

/**
 *
 * @author benyq
 * @date 4/8/2024
 *
 */
class ExoPlayerFactory(private val context: Context): IPlayer.Factory {
    @OptIn(UnstableApi::class)
    override fun create(source: MediaSource): IPlayer {
        val player = ExoVideoPlayer(context)
        player.create()
        return player
    }
}