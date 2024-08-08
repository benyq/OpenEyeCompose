package com.benyq.compose.open.eye.base.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.TypedValue
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.preferences.preferencesDataStore

lateinit var appContext: Context

fun Context.getNetworkType(): NetworkType {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.NONE
    }
}


enum class NetworkType {
    WIFI, CELLULAR, NONE
}

val Context.dataStore by preferencesDataStore(name = "settings")


fun Context.getSystemBrightness(): Float {
    var systemBrightness = 0
    try {
        systemBrightness =
            Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: SettingNotFoundException) {
        e.printStackTrace()
    }
    return systemBrightness / getSystemSettingBrightnessMax()
}


fun getSystemSettingBrightnessMax(): Float {
    try {
        val res = Resources.getSystem()
        val resId = res.getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        if (resId != 0) {
            return res.getInteger(resId).toFloat()
        }
    } catch (e: Exception) { /* ignore */
    }
    return 255f
}


/**
 * dp to px
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.px: Float
    get() = this * Resources.getSystem().displayMetrics.density

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)



fun Activity.fullScreen(fullScreen: Boolean = true) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    if (fullScreen) {
        setDecorFitsSystemWindows(window, false)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        //允许window 的内容可以上移到刘海屏状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }else {
        setDecorFitsSystemWindows(window, true)
        insetsController.show(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    }

}