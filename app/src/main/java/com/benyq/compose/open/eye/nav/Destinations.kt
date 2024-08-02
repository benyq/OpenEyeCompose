package com.benyq.compose.open.eye.nav

sealed class Destinations(val path: String) {
    data object Splash : Destinations("Splash")
    data object Main : Destinations("Main")
}