package com.benyq.compose.open.eye.business.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benyq.compose.open.eye.R
import com.benyq.compose.open.eye.base.tools.showToast
import com.benyq.compose.open.eye.business.daily.DailyScreen
import com.benyq.compose.open.eye.business.disvocery.DiscoverScreen
import com.benyq.compose.open.eye.business.hot.HotScreen
import com.benyq.compose.open.eye.business.mine.MineScreen
import com.benyq.compose.open.eye.common.SetAppearanceStatusBar
import com.benyq.compose.open.eye.common.noRippleClick
import com.benyq.compose.open.eye.nav.Destinations
import com.benyq.compose.open.eye.nav.LocalNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    SetAppearanceStatusBar(true)
    MainBackPressHandler {
        showToast("Hello")
    }
    val coroutine = rememberCoroutineScope()
    val pageState = rememberPagerState { 4 }
    var bottomIndex by remember {
        mutableIntStateOf(0)
    }
    val bottomItems = listOf("日报", "发现", "热门", "我的")

    suspend fun onItemClick(index: Int) {
        pageState.scrollToPage(index)
        bottomIndex = index
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            HorizontalPager(
                state = pageState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = false
            ) {
                when (it) {
                    0 -> DailyScreen()
                    1 -> DiscoverScreen()
                    2 -> HotScreen()
                    3 -> MineScreen()
                }
            }
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .background(Color(0xFFDCDCDC))
                    .padding(vertical = 8.dp)
            ) {
                BottomItem(
                    bottomItems[0],
                    normalImage = painterResource(id = R.drawable.home_ic_normal),
                    selectedImage = painterResource(id = R.drawable.home_ic_selected),
                    selected = bottomIndex == 0,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClick { coroutine.launch { onItemClick(0) } }
                )
                BottomItem(
                    bottomItems[1],
                    normalImage = painterResource(id = R.drawable.home_ic_discovery_normal),
                    selectedImage = painterResource(id = R.drawable.home_ic_discovery_selected),
                    selected = bottomIndex == 1,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClick { coroutine.launch { onItemClick(1) } }
                )
                BottomItem(
                    bottomItems[2],
                    normalImage = painterResource(id = R.drawable.home_ic_hot_normal),
                    selectedImage = painterResource(id = R.drawable.home_ic_hot_selected),
                    selected = bottomIndex == 2,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClick { coroutine.launch { onItemClick(2) } }
                )
                BottomItem(
                    bottomItems[3],
                    normalImage = painterResource(id = R.drawable.home_ic_mine_normal),
                    selectedImage = painterResource(id = R.drawable.home_ic_mine_selected),
                    selected = bottomIndex == 3,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClick { coroutine.launch { onItemClick(3) } }
                )
            }
        }
    }


}

@Composable
private fun BottomItem(
    title: String,
    normalImage: Painter,
    selectedImage: Painter,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = if (selected) selectedImage else normalImage,
            contentDescription = title,
            modifier = Modifier.size(25.dp),
            tint = if (selected) Color.Black else Color(0xFF9A9A9A)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = if (selected) Color.Black else Color(0xFF9A9A9A)
        )
    }
}


@Composable
private fun MainBackPressHandler(action: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val navController = LocalNavController.current
    var backPressTime = 0L
    var enabled by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    BackHandler(enabled = enabled) {
        if (navController.currentBackStackEntry?.destination?.route == Destinations.Main.path) {
            // 当前页面是最后一个页面，可以关闭应用或者执行其他操作
            val currentTime = System.currentTimeMillis()
            val timeInterval = System.currentTimeMillis() - backPressTime

            if (timeInterval > 2000) {
                action()
                backPressTime = currentTime
            } else {
                enabled = false
                (context as ComponentActivity).finish()
            }
        } else {
            // 当前页面不是最后一个页面，执行正常的返回操作
            navController.popBackStack()
        }
    }
}


@Composable
@Preview
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}