package com.benyq.compose.open.eye.scene.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.cache.CacheWriter
import com.benyq.compose.open.eye.base.common.L
import com.benyq.tikbili.player.download.BasePreloadTask
import com.benyq.tikbili.player.download.IPreloadTask
import com.benyq.tikbili.player.download.IPreloadTask.PreloadState
import com.benyq.tikbili.player.source.MediaSource

/**
 *
 * @author benyq
 * @date 5/6/2024
 *
 */

class ExoPlayerPreloadTaskFactory(private val context: Context): IPreloadTask.Factory {
    @OptIn(UnstableApi::class)
    override fun create(): IPreloadTask {
        return ExoPlayerPreloadTask(context)
    }
}

@UnstableApi
class ExoPlayerPreloadTask(private val context: Context): BasePreloadTask() {

    private lateinit var cacheWriter: CacheWriter

    @OptIn(UnstableApi::class)
    override fun start() {
        val dataSource = getDataSource()
        val videoUri = Uri.parse(dataSource.url)
        val dataSpec = DataSpec(videoUri, 0, resolveLength(dataSource), dataSource.getUniqueId())
        cacheWriter = CacheWriter(MediaCacheFactory.getCacheFactory(context.applicationContext).createDataSource(), dataSpec, null, object : CacheWriter.ProgressListener {
            override fun onProgress(p0: Long, p1: Long, p2: Long) {
                L.d(this@ExoPlayerPreloadTask, "onProgress", p0, p1, p2)
            }
        })
        try {
            cacheWriter.cache()
            setState(PreloadState.LOADING)
        }catch (e: Exception) {
            setState(PreloadState.ERROR)
        }
    }

    override fun cancel() {
        cacheWriter.cancel()
        setState(PreloadState.RELEASED)
    }

    override fun cacheKey(): String {
        return getDataSource().getUniqueId()
    }

    private fun resolveLength(dataSource: MediaSource): Long {
        val l = dataSource.getExtra(IPreloadTask.EXTRA_PRELOAD_SIZE_IN_BYTES, Long::class.javaObjectType) ?: 0L
        return if (l > 0) l else IPreloadTask.DEFAULT_PRELOAD_SIZE
    }
}