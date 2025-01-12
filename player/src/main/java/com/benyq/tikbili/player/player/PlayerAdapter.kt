package com.benyq.tikbili.player.player

import android.os.Looper
import com.benyq.compose.open.eye.base.common.L
import com.benyq.tikbili.player.dispather.EventDispatcher
import com.benyq.tikbili.player.player.IPlayer
import com.benyq.tikbili.player.player.IPlayer.PlayState

/**
 *
 * @author benyq
 * @date 4/9/2024
 * IPlayer 适配器，切换内核的时候使用
 */
abstract class PlayerAdapter: IPlayer {

    protected val _dispatcher = EventDispatcher(Looper.getMainLooper())
    protected var playState: PlayState = PlayState.STATE_IDLE

    override fun getState(): PlayState {
        return playState
    }

    override fun addPlayerListener(listener: EventDispatcher.EventListener) {
        _dispatcher.addSubscriber(listener)
    }

    override fun removePlayerListener(listener: EventDispatcher.EventListener) {
        _dispatcher.removeSubscriber(listener)
    }

    override fun setState(state: PlayState) {
        this.playState = state
    }

    override fun isReleased(): Boolean {
        return playState == PlayState.STATE_RELEASED
    }

    override fun isIDLE(): Boolean {
        return playState == PlayState.STATE_IDLE
    }

    override fun isCompleted(): Boolean {
        return playState == PlayState.STATE_COMPLETED
    }

    override fun isPlaying(): Boolean {
        return playState == PlayState.STATE_STARTED
    }

    override fun isPaused(): Boolean {
        return playState == PlayState.STATE_PAUSED
    }

    override fun isError(): Boolean {
        return playState == PlayState.STATE_ERROR
    }

    override fun isPreparing(): Boolean {
        return playState == PlayState.STATE_PREPARING
    }

    override fun isInPlaybackState(): Boolean {
        when (playState) {
            PlayState.STATE_PREPARED, PlayState.STATE_STARTED, PlayState.STATE_PAUSED, PlayState.STATE_COMPLETED -> return true
            else -> {}
        }
        return false
    }

    protected fun checkIsRelease(func: String): Boolean {
        if (isReleased()) {
            L.e(this, func, "already released!")
            return true
        }
        return false
    }
}