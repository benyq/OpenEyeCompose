package com.benyq.compose.open.eye.scene.layer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
import com.benyq.compose.open.eye.base.common.px
import com.benyq.compose.open.eye.scene.PlayScene
import com.benyq.tikbili.player.R
import com.benyq.tikbili.player.playback.layer.base.AnimateLayer

/**
 *
 * @author benyq
 * @date 4/29/2024
 *
 */
class PlayPauseLayer: AnimateLayer() {
    override val tag: String = "PlayPauseLayer"
    private var ivPlayback: ImageView? = null

    override fun onCreateLayerView(parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_pasue_layer, parent, false)
        ivPlayback = view as ImageView
        view.setOnClickListener {
            val player = player() ?: return@setOnClickListener
            val isPlaying = player.isPlaying()
            changeIcon(!isPlaying)
            if (isPlaying) {
                player.pause()
            }else {
                player.start()
            }
            layerHost()?.findLayer(GestureLayer::class.java)?.showController()
        }
        return view
    }

    override fun show() {
        super.show()
        setState()
        syncTheme()
    }

    override fun onVideoViewPlaySceneChanged(fromScene: Int, toScene: Int) {
        setState()
        syncTheme()
    }

    private fun changeIcon(isPlaying: Boolean) {
        if (isPlaying) {
            ivPlayback?.setImageResource(R.drawable.ic_pause)
        }else {
            ivPlayback?.setImageResource(R.drawable.ic_play)
        }
    }

    private fun syncTheme() {
        val playButton = getView() ?: return
        val videoView = videoView() ?: return
        if (videoView.getPlayScene() == PlayScene.SCENE_FULLSCREEN) {
            playButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                width = 48.px
                height = 48.px
            }
        } else {
            playButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                width = 30.px
                height = 30.px
            }
        }
    }

    private fun setState() {
        val player = player() ?: return
        changeIcon(player.isPlaying())
    }
}