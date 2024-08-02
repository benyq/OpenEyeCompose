package com.benyq.compose.open.eye.business.mine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun MineScreen(){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "我的")
    }
}