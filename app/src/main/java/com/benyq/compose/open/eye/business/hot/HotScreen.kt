package com.benyq.compose.open.eye.business.hot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun HotScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "人气榜")
    }
}