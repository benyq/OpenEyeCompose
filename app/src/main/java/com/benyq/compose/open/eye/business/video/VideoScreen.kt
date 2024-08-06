package com.benyq.compose.open.eye.business.video

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.preferences.protobuf.Internal.BooleanList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.benyq.compose.open.eye.R
import com.benyq.compose.open.eye.business.AppConfig
import com.benyq.compose.open.eye.common.L
import com.benyq.compose.open.eye.common.NetworkType
import com.benyq.compose.open.eye.common.SetAppearanceStatusBar
import com.benyq.compose.open.eye.common.getNetworkType
import com.benyq.compose.open.eye.common.noRippleClick
import com.benyq.compose.open.eye.common.widget.Error
import com.benyq.compose.open.eye.model.ItemData
import com.benyq.compose.open.eye.model.ReplyData
import com.benyq.compose.open.eye.nav.Destinations
import com.benyq.compose.open.eye.nav.LocalNavController
import com.benyq.compose.open.eye.tools.DateTool
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoScreen(viewModel: VideoViewModel = viewModel()) {
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    val itemData by viewModel.itemData.collectAsState()

    SetAppearanceStatusBar(false)
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(top = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClick {
                            navController.navigateUp()
                        }
                )
                Image(
                    painter = rememberAsyncImagePainter(model = itemData.author?.icon),
                    contentDescription = null, modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(text = itemData.author?.name ?: "未知", color = Color.White)
                Icon(imageVector = Icons.Rounded.Share, contentDescription = "分享")
            }

            Video(
                itemData.playUrl,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )

            val pagerState = rememberPagerState { 2 }
            val items = listOf("简介", "评论")

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = Color.Black,
                containerColor = Color.Black,
                edgePadding = 0.dp,
                modifier = Modifier,
                indicator = { positions ->//设置滑动条的属性，默认是白色的
                    SecondaryIndicator(
                        Modifier
                            .tabIndicatorOffset(positions[pagerState.currentPage])
                            .width(10.dp)
                            .height(1.dp),
                        color = Color.Red
                    )
                },
                divider = { Spacer(Modifier.fillMaxWidth()) }
            ) {
                items.mapIndexed { index, item ->
                    Tab(
                        modifier = Modifier.width(10.dp),
                        selected = index == pagerState.currentPage,
                        text = {
                            Text(text = item, color = Color.White)
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.5f)
            )

            LaunchedEffect(itemData.id) {
                viewModel.getRelateVideoList(itemData.id)
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (it) {
                    0 -> VideoDetail(itemData, viewModel, modifier = Modifier.fillMaxSize())
                    1 -> VideoComment(viewModel, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun VideoDetail(
    itemData: ItemData,
    viewModel: VideoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.container.uiStateFlow.collectAsState()

    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)) {
                Text(text = itemData.title, color = Color.White)
                Text(
                    text = "#${itemData.category}",
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 5.dp)
                )

                AnimatedText(itemData.description, modifier = Modifier.padding(top = 5.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ConsumptionItem(
                        itemData.consumption.collectionCount.toString(), painter = painterResource(
                            id = R.drawable.ic_thumb_up_border_24
                        )
                    )
                    ConsumptionItem(
                        "收藏", painter = painterResource(
                            id = R.drawable.ic_round_star_border_24
                        )
                    )
                    ConsumptionItem(
                        itemData.consumption.replyCount.toString(), painter = painterResource(
                            id = R.drawable.ic_round_comment_24
                        )
                    )
                    ConsumptionItem(
                        "缓存", painter = painterResource(
                            id = R.drawable.ic_round_download_24
                        )
                    )
                    ConsumptionItem(
                        itemData.consumption.shareCount.toString(), painter = painterResource(
                            id = R.drawable.ic_ios_share_24
                        )
                    )
                }

            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        items(uiState.relateVideo.size) {
            val data = uiState.relateVideo[it]
            VideoRelativeItem(
                item = data,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                viewModel.setItemData(data)
            }
        }

        item {
            VideoItemEnd()
        }
    }
}

@Composable
private fun VideoRelativeItem(item: ItemData, modifier: Modifier = Modifier, action: () -> Unit) {
    Column(modifier = modifier.noRippleClick { action() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = item.author?.icon),
                contentDescription = null, modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = item.title, color = Color.White, fontSize = 12.sp, maxLines = 1)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = item.author?.name ?: "未知", color = Color.White, fontSize = 10.sp)
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = DateTool.formatVideoDuration(item.duration * 1000L),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = item.cover.feed),
                contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ConsumptionItem(title: String, painter: Painter, action: () -> Unit = {}) {
    Column(
        modifier = Modifier.noRippleClick { action() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painter, tint = Color.White, contentDescription = null)
        Text(text = title, color = Color.White)
    }
}

@Composable
private fun VideoComment(viewModel: VideoViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.container.uiStateFlow.collectAsState()
    val replyState by viewModel.currentReply.collectAsState()

    val latestVideoReplyPaging = viewModel.replyPageFlow.collectAsLazyPagingItems()
    val hotVideoReplyPaging = viewModel.replyHotPageFlow.collectAsLazyPagingItems()

    val currentReply =
        if (replyState.type == VideoReply.LATEST) latestVideoReplyPaging else hotVideoReplyPaging

    LaunchedEffect(replyState) {
        currentReply.refresh()
    }

    Column(
        modifier = modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = uiState.replyType.getTitle(), color = Color.White)
            Row(modifier = Modifier) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = uiState.replyType.getTypeString(),
                    color = Color.White,
                    modifier = Modifier.noRippleClick {
                        viewModel.changeVideoReplyType()
                    })
            }
        }
        AnimatedVisibility(
            visible = currentReply.loadState.refresh is LoadState.Loading,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp)
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {

            items(currentReply.itemCount) {
                val reply = currentReply[it] ?: return@items
                VideoCommentItem(reply, Modifier)
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    thickness = 0.5.dp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            when (currentReply.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                }

                is LoadState.Error -> {
                    item {
                        Error(modifier = Modifier.fillMaxWidth(), color = Color.White, retry = {
                            currentReply.retry()
                        })
                    }
                }

                else -> {
                    item {
                        VideoItemEnd()
                    }
                }
            }
        }
    }
}


@Composable
private fun VideoCommentItem(
    reply: ReplyData,
    modifier: Modifier = Modifier,
    userAction: () -> Unit = {},
    replyAction: () -> Unit = {},
    thumbUpAction: () -> Unit = {},
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = reply.user.avatar),
            contentDescription = null, modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .noRippleClick {
                    userAction()
                }
        )
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .noRippleClick { replyAction() }) {
            Text(text = reply.user.nickname, color = Color.White, fontSize = 14.sp)
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                ) {
                    append(reply.message)
                }
                append("  ")
                withStyle(
                    style = SpanStyle(
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                ) {
                    append(DateTool.formatVideoDuration(reply.createTime, "yyyy/MM/dd"))
                }
            }
            Text(text = annotatedString, color = Color.White)
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .noRippleClick { thumbUpAction() },
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = if (reply.liked) R.drawable.ic_thumb_up_24 else R.drawable.ic_thumb_up_border_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(text = reply.likeCount.toString(), color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
private fun VideoItemEnd() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text(text = "End~", color = Color.White, modifier = Modifier.align(Alignment.Center))
    }
}


@Composable
private fun Video(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    fun isAutoPlay(): Boolean {
        val networkType = context.getNetworkType()
        return when(networkType) {
            NetworkType.WIFI -> AppConfig.getAutoPlayOnWIFI()
            NetworkType.CELLULAR -> AppConfig.getAutoPlayOnMobile()
            NetworkType.NONE -> false
        }
    }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = isAutoPlay()
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        // PlayerView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { context ->
                PlayerView(context).apply {
                    this.player = exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams
                                .MATCH_PARENT,
                            ViewGroup.LayoutParams
                                .MATCH_PARENT
                        )
                }
            }
        )
    }

    LaunchedEffect(url) {
        exoPlayer.run {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            seekTo(0L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

}

@Composable
fun AnimatedText(targetText: String, modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = visible, label = "visibility")
    val progress by transition.animateFloat(
        transitionSpec = { if (targetState) tween(durationMillis = 1000) else snap() },
        label = "progress"
    ) { state ->
        if (state) 1f else 0f
    }
    LaunchedEffect(targetText) {
        if (visible) {
            visible = false
            awaitFrame()
            visible = true
        }else {
            visible = true
        }
    }

    val visibleTextLength = (targetText.length * progress).toInt()
    val visibleText = targetText.take(visibleTextLength)

    Text(
        text = buildAnnotatedString {
            append(visibleText)
            withStyle(style = SpanStyle(color = Color.Transparent)) {
                append(targetText.drop(visibleTextLength))
            }
        },
        modifier = modifier,
        fontSize = 16.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}


@Composable
@Preview
fun VideoScreenPreview() {
    VideoScreen()
}

