package com.benyq.compose.open.eye

import android.app.Application
import com.benyq.compose.open.eye.base.common.L
import com.benyq.compose.open.eye.base.common.appContext
import com.benyq.compose.open.eye.scene.player.ExoPlayerCacheLoader
import com.benyq.compose.open.eye.scene.player.ExoPlayerFactory
import com.benyq.compose.open.eye.scene.player.ExoPlayerPreloadTaskFactory
import com.benyq.tikbili.player.download.CacheLoader
import com.benyq.tikbili.player.player.IPlayer

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        L.ENABLE_LOG = true
        appContext = applicationContext

        IPlayer.Factory.Default.set(ExoPlayerFactory(this))
        CacheLoader.Default.set(ExoPlayerCacheLoader(this, ExoPlayerPreloadTaskFactory(this)))
    }
}