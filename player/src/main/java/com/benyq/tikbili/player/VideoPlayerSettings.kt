package com.benyq.tikbili.player

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.benyq.compose.open.eye.base.common.appContext
import com.benyq.compose.open.eye.base.common.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 *
 * @author benyq
 * @date 5/7/2024
 *
 */
object VideoPlayerSettings {

    private val coroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    const val PLAY_LOOPING = 0
    const val PLAY_NEXT = 1
    private val playBackCompleteActionKey = intPreferencesKey("playback_complete_action")

    fun playBackCompleteAction(): Int {
        return runBlocking {
            appContext.dataStore.data.first()[playBackCompleteActionKey] ?: PLAY_LOOPING
        }
    }

    fun setPlayBackCompleteAction(action: Int) {
        if (action == PLAY_LOOPING || action == PLAY_NEXT) {
            coroutineScope.launch {
                appContext.dataStore.edit { settings ->
                    settings[playBackCompleteActionKey] = action
                }
            }
        }else {
            throw IllegalArgumentException("action must be PLAY_LOOPING or PLAY_NEXT")
        }
    }
}