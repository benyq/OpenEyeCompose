package com.benyq.compose.open.eye.business.daily

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.benyq.compose.open.eye.common.noRippleClick
import com.benyq.compose.open.eye.model.Item
import com.benyq.compose.open.eye.model.ItemData
import com.benyq.compose.open.eye.tools.DateTool
import com.benyq.compose.open.eye.ui.theme.Black54
import com.benyq.compose.open.eye.ui.theme.White54
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DailyScreen(viewModel: DailyViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = "日报",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "搜索",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
                    .noRippleClick {

                    }
            )
        }
        val dailyData = viewModel.pageFlow.collectAsLazyPagingItems()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = dailyData.loadState.refresh == LoadState.Loading,
            onRefresh = {
                dailyData.refresh()
            })

        Box(
            modifier = Modifier
                .weight(1f)
        ) {

            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                when (dailyData.loadState.refresh) {
                    is LoadState.Loading -> {}
                    is LoadState.Error -> {
                        item {
                            Text(text = "加载失败1")
                        }
                    }

                    is LoadState.NotLoading -> {}
                }

                items(count = dailyData.itemCount) { index ->
                    val itemData = dailyData[index]
                    itemData?.let {
                        if (index == 0) {
                            BannerComponent(viewModel.bannerList)
                        } else if (itemData.type == "textHeader") {
                            TitleItemWidget(it.data)
                        } else {
                            DailyItem(it.data)
                        }
                    }
                }

                when (dailyData.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator()
                        }
                    }

                    is LoadState.Error -> {
                        val state = dailyData.loadState.append as LoadState.Error
                        item {
                            Text(text = "加载失败2:${state.error}")
                        }
                    }

                    is LoadState.NotLoading -> {}
                    else -> {}
                }
            }

            PullRefreshIndicator(refreshing = dailyData.loadState.refresh == LoadState.Loading,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = -160f
                    })
        }

    }
}


@Composable
private fun DailyItem(item: ItemData?, modifier: Modifier = Modifier) {
    if (item == null) return
    Column(modifier = modifier) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = item.cover.feed),
                contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop
            )

            Text(
                text = item.category,
                modifier = Modifier
                    .padding(15.dp)
                    .clip(CircleShape)
                    .background(White54)
                    .padding(10.dp)
                    .align(Alignment.TopStart),
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = DateTool.formatVideoDuration(item.duration * 1000L),
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Black54)
                    .padding(5.dp)
                    .align(Alignment.BottomEnd),
                color = Color.White, fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = item.author?.icon),
                contentDescription = null, modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(text = item.author?.name ?: "未知", color = Color.LightGray)
            }
            Icon(imageVector = Icons.Rounded.Share, contentDescription = "分享")
        }
    }
}

@Composable
fun TitleItemWidget(itemData: ItemData?) {
    if (itemData == null) return
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = itemData.text ?: "",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerComponent(banners: List<Item>) {

    // compose 1.6.8 依然会有这个bug， 所以要除 10000 https://issuetracker.google.com/issues/326887746
    val virtualCount = Int.MAX_VALUE / 10000
    val actualCount = banners.size
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState(initialPage = 0) { virtualCount }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    ) {

        HorizontalPager(state = pageState) { index ->
            val actualIndex = index % actualCount
            Image(painter = rememberAsyncImagePainter(model = banners[actualIndex].data!!.cover.feed),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(7 / 3f)
                    .graphicsLayer {
                        val scale = 1f - abs(pageState.currentPageOffsetFraction) * 0.5f
                        scaleX = scale
                        scaleY = scale
                    }
                    .noRippleClick {

                    })

            DisposableEffect(Unit) {
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        coroutineScope.launch {
                            pageState.animateScrollToPage(pageState.currentPage + 1)
                        }
                    }
                }, 3000, 3000)
                onDispose {
                    timer.cancel()
                }
            }
        }

        Row(
            Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(Color(0x33000000))
                .padding(start = 10.dp, end = 10.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val actualIndex = pageState.currentPage % actualCount
            Text(text = banners[actualIndex].data!!.title, fontSize = 12.sp, color = Color.White)
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(banners.size) { iteration ->
                    val color = if (actualIndex == iteration) Color.White else Color(0x99FFFFFF)
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}
