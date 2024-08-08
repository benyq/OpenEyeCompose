package com.benyq.compose.open.eye.scene.layer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.benyq.compose.open.eye.R
import com.benyq.compose.open.eye.base.common.px
import com.benyq.compose.open.eye.scene.event.ActionBackEvent
import com.benyq.compose.open.eye.scene.PlayScene
import com.benyq.compose.open.eye.scene.event.ActionMoreActionEvent
import com.benyq.compose.open.eye.scene.source.VideoItem
import com.benyq.tikbili.player.playback.layer.base.AnimateLayer

/**
 *
 * @author benyq
 * @date 4/26/2024
 *
 */
class TitleBarLayer: AnimateLayer() {
    override val tag: String = "TitleLayer"

    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var titleBar: View? = null
    private var ivMore: ImageView? = null

    override fun onCreateLayerView(parent: ViewGroup): View? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_title_bar_layer, parent, false)

        ivBack = view.findViewById(R.id.back)
        tvTitle = view.findViewById(R.id.title)
        titleBar = view.findViewById(R.id.title_bar)
        ivMore = view.findViewById(R.id.more)

        ivBack?.setOnClickListener {
            controller()?.dispatcher()?.obtain(ActionBackEvent::class.java)?.dispatch()
        }
        ivMore?.setOnClickListener {
            controller()?.dispatcher()?.obtain(ActionMoreActionEvent::class.java)?.dispatch()
        }

        return view
    }

    override fun show() {
        super.show()
        tvTitle?.text = resolveTitle()
        applyTheme()
    }

    override fun onVideoViewPlaySceneChanged(fromScene: Int, toScene: Int) {
        applyTheme()
        if (!checkShow()) {
            dismiss()
        }
    }

    private fun checkShow(): Boolean {
        return when (playScene()) {
            PlayScene.SCENE_FULLSCREEN, PlayScene.SCENE_DETAIL, PlayScene.SCENE_UNKNOWN -> true
            else -> false
        }
    }

    private fun resolveTitle(): String {
        val source = dataSource()?: return ""
        return VideoItem.get(source)?.title ?: ""
    }

    private fun applyTheme() {
        if (playScene() == PlayScene.SCENE_FULLSCREEN) {
            applyFullScreenTheme()
        } else {
            applyHalfScreenTheme()
        }
    }

    private fun applyFullScreenTheme() {
        setTitleBarLeftRightMargin(44)
        tvTitle?.visibility = View.VISIBLE
        ivBack?.visibility = View.VISIBLE
        ivMore?.visibility = View.GONE
    }

    private fun applyHalfScreenTheme() {
        setTitleBarLeftRightMargin(0)
        tvTitle?.visibility = View.GONE
        ivBack?.visibility = View.GONE
        ivMore?.visibility = View.VISIBLE
    }

    private fun setTitleBarLeftRightMargin(marginDp: Int) {
        val margin = marginDp.px
        titleBar?.updateLayoutParams<FrameLayout.LayoutParams> {
            marginStart = margin
            marginEnd = margin
        }
    }
}