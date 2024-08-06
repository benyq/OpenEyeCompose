package com.benyq.compose.open.eye.common.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benyq.compose.open.eye.common.noRippleClick
import com.benyq.compose.open.eye.ui.theme.Black38


@Composable
fun Loading(modifier: Modifier = Modifier, title: String = "正在加载") {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CircularProgressIndicator(color = Black38)
        Text(text = title)
    }
}


@Composable
fun Error(modifier: Modifier = Modifier, title: String = "加载失败",
          color: Color = Color.Black,
          retry: ()->Unit = {}) {
    Column(
        modifier = modifier.noRippleClick { retry() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = title, color = color)
    }
}

@Composable
@Preview
fun LoadingPreview() {
    Loading()
}

@Composable
@Preview
fun ErrorPreview() {
    Error()
}