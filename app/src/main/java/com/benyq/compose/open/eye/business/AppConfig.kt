package com.benyq.compose.open.eye.business

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.benyq.compose.open.eye.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


val Context.dataStore by preferencesDataStore(name = "settings")

object AppConfig {

    private val KEY_AUTO_PLAY_ON_WIFI = booleanPreferencesKey("autoPlayOnWIFI")
    private val KEY_AUTO_PLAY_ON_MOBILE = booleanPreferencesKey("autoPlayOnMobile")

    fun getAutoPlayOnWIFI(): Boolean {
        return runBlocking {
            App.CONTEXT.dataStore.data.first()[KEY_AUTO_PLAY_ON_WIFI] ?: false
        }
    }

    fun getAutoPlayOnWIFIFlow() = App.CONTEXT.dataStore.data.map {
        it[KEY_AUTO_PLAY_ON_WIFI] ?: false
    }

    fun setAutoPlayOnWIFI(coroutineScope: CoroutineScope, autoPlay: Boolean) {
        coroutineScope.launch {
            App.CONTEXT.dataStore.edit { settings ->
                settings[KEY_AUTO_PLAY_ON_WIFI] = autoPlay
            }
        }
    }

    fun getAutoPlayOnMobile(): Boolean {
        return runBlocking {
            App.CONTEXT.dataStore.data.first()[KEY_AUTO_PLAY_ON_MOBILE] ?: false
        }
    }

    fun getAutoPlayOnMobileFlow() = App.CONTEXT.dataStore.data.map {
        it[KEY_AUTO_PLAY_ON_MOBILE] ?: false
    }

    fun setAutoPlayOnMobile(coroutineScope: CoroutineScope, autoPlay: Boolean) {
        coroutineScope.launch {
            App.CONTEXT.dataStore.edit { settings ->
                settings[KEY_AUTO_PLAY_ON_MOBILE] = autoPlay
            }
        }
    }

}

