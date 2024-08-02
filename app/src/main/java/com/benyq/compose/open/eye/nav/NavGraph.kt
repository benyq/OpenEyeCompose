package com.benyq.compose.open.eye.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.benyq.compose.open.eye.business.SplashScreen
import com.benyq.compose.open.eye.business.main.MainScreen

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided") }


@Composable
fun NavGraph(startDestination: String = Destinations.Splash.path) {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Destinations.Splash.path) {
                SplashScreen()
            }
            composable(Destinations.Main.path) {
                MainScreen()
            }
        }
    }

}