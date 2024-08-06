package com.benyq.compose.open.eye.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.benyq.compose.open.eye.business.SplashScreen
import com.benyq.compose.open.eye.business.daily.DailyScreen
import com.benyq.compose.open.eye.business.main.MainScreen
import com.benyq.compose.open.eye.business.video.VideoScreen
import com.benyq.compose.open.eye.business.video.VideoSettingScreen
import com.benyq.compose.open.eye.business.video.VideoViewModel
import com.benyq.compose.open.eye.common.L
import com.benyq.compose.open.eye.model.ItemData
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }


@Composable
fun NavGraph(startDestination: String = Destinations.Splash.path) {
    val navController = rememberNavController()
    val gson = remember { Gson() }

    CompositionLocalProvider(
        LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Destinations.Splash.path) {
                SplashScreen()
            }
            composable(Destinations.Main.path) {
                MainScreen()
            }
            composable(Destinations.Video.path) {
                val dataJson = it.arguments?.getString("data") ?: ""
                val json = URLDecoder.decode(dataJson, "utf-8")
                val data = gson.fromJson(json, ItemData::class.java)
                val viewModel: VideoViewModel = viewModel()
                viewModel.setItemData(data)
                VideoScreen(viewModel)
            }
            composable(Destinations.VideoSetting.path) {
                VideoSettingScreen()
            }
        }
    }

}