package com.benyq.compose.open.eye.scene.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.SimpleCache
import com.benyq.tikbili.player.download.CacheLoader
import com.benyq.tikbili.player.download.IPreloadTask
import com.benyq.tikbili.player.source.MediaSource
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 * @author benyq
 * @date 5/7/2024
 *
 */
class ExoPlayerCacheLoader(private val context: Context, private val taskFactory: IPreloadTask.Factory): CacheLoader {

    private val preloadTasks = CopyOnWriteArrayList<IPreloadTask>()

    override fun createTask(): IPreloadTask {
        return taskFactory.create()
    }

    override fun preload(source: MediaSource) {
        val task = preloadTasks.firstOrNull { it.cacheKey() == source.getUniqueId() } ?: let {
            val newTask = createTask()
            preloadTasks.add(newTask)
            newTask
        }
        task.setDataSource(source)
        task.start()
    }

    override fun stopTask(source: MediaSource) {
        stopTask(source.getUniqueId())
    }

    override fun stopTask(mediaId: String) {
        for (i in preloadTasks.size - 1 downTo 0) {
            val task = preloadTasks[i]
            if (task.cacheKey() == mediaId) {
                task.cancel()
                preloadTasks.remove(task)
            }
        }
    }

    override fun stopAllTask() {
        preloadTasks.forEach {
            it.cancel()
        }
        preloadTasks.clear()
    }

    @OptIn(UnstableApi::class)
    override fun clearCache() {
        val simpleCache = MediaCacheFactory.getCacheFactory(context).cache as? SimpleCache ?: return
        simpleCache.release()
    }
}