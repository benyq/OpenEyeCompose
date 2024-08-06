package com.benyq.compose.open.eye.nav

sealed class Destinations(val path: String) {
    data object Splash : Destinations("Splash")
    data object Main : Destinations("Main")
    data object Video : Destinations("Video/?data={data}") {
        fun createRoute(data: String) = path.replace("{data}", data)
    }
    data object VideoSetting : Destinations("VideoSetting")
}