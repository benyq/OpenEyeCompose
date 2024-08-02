package com.benyq.compose.open.eye

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.benyq.compose.open.eye.common.L

class App: Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT : Context
    }
    override fun onCreate() {
        super.onCreate()
        L.ENABLE_LOG = true
        CONTEXT = applicationContext
    }
}