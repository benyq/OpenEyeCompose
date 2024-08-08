package com.benyq.compose.open.eye.business

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.benyq.compose.open.eye.base.common.NetworkType
import com.benyq.compose.open.eye.base.common.appContext
import com.benyq.compose.open.eye.base.common.dataStore
import com.benyq.compose.open.eye.base.common.getNetworkType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object AppConfig {

    private val KEY_AUTO_PLAY_ON_WIFI = booleanPreferencesKey("autoPlayOnWIFI")
    private val KEY_AUTO_PLAY_ON_MOBILE = booleanPreferencesKey("autoPlayOnMobile")

    fun getAutoPlayOnWIFI(): Boolean {
        return runBlocking {
            appContext.dataStore.data.first()[KEY_AUTO_PLAY_ON_WIFI] ?: false
        }
    }

    fun getAutoPlayOnWIFIFlow() = appContext.dataStore.data.map {
        it[KEY_AUTO_PLAY_ON_WIFI] ?: false
    }

    fun setAutoPlayOnWIFI(coroutineScope: CoroutineScope, autoPlay: Boolean) {
        coroutineScope.launch {
            appContext.dataStore.edit { settings ->
                settings[KEY_AUTO_PLAY_ON_WIFI] = autoPlay
            }
        }
    }

    fun getAutoPlayOnMobile(): Boolean {
        return runBlocking {
            appContext.dataStore.data.first()[KEY_AUTO_PLAY_ON_MOBILE] ?: false
        }
    }

    fun getAutoPlayOnMobileFlow() = appContext.dataStore.data.map {
        it[KEY_AUTO_PLAY_ON_MOBILE] ?: false
    }

    fun setAutoPlayOnMobile(coroutineScope: CoroutineScope, autoPlay: Boolean) {
        coroutineScope.launch {
            appContext.dataStore.edit { settings ->
                settings[KEY_AUTO_PLAY_ON_MOBILE] = autoPlay
            }
        }
    }

    fun isAutoPlay(context: Context): Boolean {
        val networkType = context.getNetworkType()
        return when(networkType) {
            NetworkType.WIFI -> getAutoPlayOnWIFI()
            NetworkType.CELLULAR -> getAutoPlayOnMobile()
            NetworkType.NONE -> false
        }
    }


}

