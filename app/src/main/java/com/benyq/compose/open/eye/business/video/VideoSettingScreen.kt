package com.benyq.compose.open.eye.business.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benyq.compose.open.eye.business.AppConfig
import com.benyq.compose.open.eye.common.SetAppearanceStatusBar
import com.benyq.compose.open.eye.common.noRippleClick
import com.benyq.compose.open.eye.nav.LocalNavController
import com.benyq.compose.open.eye.ui.theme.OpenEyeComposeTheme


@Composable
fun VideoSettingScreen() {
    SetAppearanceStatusBar(true)
    val navController = LocalNavController.current

    val autoPlayWIFI by AppConfig.getAutoPlayOnWIFIFlow().collectAsState(false)
    val autoPlayMobile by AppConfig.getAutoPlayOnMobileFlow().collectAsState(false)
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClick {
                            navController.navigateUp()
                        }
                )
                Text(
                    text = "播放设置",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            VideoSettingItemSwitch(
                "Wi-Fi下自动播放",
                autoPlayWIFI,
                onCheckedChange = {
                    AppConfig.setAutoPlayOnWIFI(coroutineScope, it)
                }
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
            VideoSettingItemSwitch(
                "移动网络下自动播放",
                autoPlayMobile,
                onCheckedChange = {
                    AppConfig.setAutoPlayOnMobile(coroutineScope, it)
                }
            )
            HorizontalDivider(thickness = 3.dp, color = Color.White.copy(alpha = 0.3f))
        }
    }
}


@Composable
private fun VideoSettingItemSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview
@Composable
fun VideoSettingScreenPreview() {
    OpenEyeComposeTheme {
        VideoSettingScreen()
    }
}