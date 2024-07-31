package com.benyq.compose.open.eye.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {

    fun <T> create(baseUrl: String, clazz: Class<T>, init: (OkHttpClient.Builder)->Unit = {}): T {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("TAG", "log: $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttp {
                addInterceptor(loggingInterceptor)
                init(this)
            })
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(clazz)
    }

    fun createOkHttp(config: OkHttpClient.Builder.() -> Unit): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .apply {
                config()
            }
            .build()
    }
}