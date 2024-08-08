package com.benyq.compose.open.eye.business.video

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.benyq.compose.open.eye.base.common.BaseViewModel
import com.benyq.compose.open.eye.base.common.DataState
import com.benyq.compose.open.eye.base.common.mvi.UiEvent
import com.benyq.compose.open.eye.base.common.mvi.UiState
import com.benyq.compose.open.eye.base.common.mvi.extension.containers
import com.benyq.compose.open.eye.http.openEyeApi
import com.benyq.compose.open.eye.model.Author
import com.benyq.compose.open.eye.model.Consumption
import com.benyq.compose.open.eye.model.Cover
import com.benyq.compose.open.eye.model.Header
import com.benyq.compose.open.eye.model.ItemData
import com.benyq.compose.open.eye.model.Owner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class VideoViewModel: BaseViewModel() {

    val container by containers<VideoState, VideoEvent>(VideoState())

    val currentReply = container.uiStateFlow.map { it.replyType }.stateIn(viewModelScope, SharingStarted.Lazily, ReplyType(VideoReply.LATEST))

    private val _itemData = MutableStateFlow(defaultItemData)
    val itemData = _itemData.asStateFlow()

    val replyPageFlow = Pager(config = PagingConfig(pageSize = 10,
        initialLoadSize = 10,
        prefetchDistance = 2), pagingSourceFactory = {
        VideoReplyPagingSource(openEyeApi, VideoReply.LATEST) { itemData.value.id }
    }).flow.cachedIn(viewModelScope)

    val replyHotPageFlow = Pager(config = PagingConfig(pageSize = 10,
        initialLoadSize = 10,
        prefetchDistance = 2), pagingSourceFactory = {
        VideoReplyPagingSource(openEyeApi, VideoReply.HOT) { itemData.value.id }
    }).flow.cachedIn(viewModelScope)


    fun setItemData(itemData: ItemData) {
        _itemData.value = itemData
    }

    fun getRelateVideoList(id: Int) {
        executeFlow {
            openEyeApi.getRelateVideoList(id)
        }.onEach {
            when (it) {
                is DataState.Success -> {
                    container.updateState {
                        copy(relateVideo = it.data.itemList.filter { item-> item.type == "videoSmallCard" }.mapNotNull { it.data })
                    }
                }
                is DataState.Error -> {

                }
                is DataState.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun changeVideoReplyType() {
        val newReplyType = if (currentReply.value.type == VideoReply.LATEST) {
            VideoReply.HOT
        } else {
            VideoReply.LATEST
        }
        container.updateState {
            copy(replyType = ReplyType(newReplyType))
        }
    }

}

private val defaultItemData = ItemData(
    id = 1,
    title = "标题",
    description = "描述",
    cover = Cover(
        feed = "https://img.zcool.cn/community/01f05e5b9e6b0a6a49f0f7f0f0f0f0.png@1280w_1l_2o_100sh.png",
        detail = "https://img.zcool.cn/community/01f05e5b9e6b0a6a49f0f7f0f0f0f0.png@1280w_1l_2o_100sh.png",
        blurred = "https://img.zcool.cn/community/01f05e5b9e6b0a6a49f0f7f0f0f0f0.png@1280w_1l_2o_100sh.png",
    ),
    playUrl = "https://img.zcool.cn/community/01f05e5b9e6b0a6a49f0f7f0f0f0f0.png@1280w_1l_2o_100sh.png",
    duration = 0,
    category = "分类",
    urls = listOf(),
    consumption = Consumption(0, 0, 0),
    dataType = "",
    height = 0,
    width = 0,
    owner = Owner("", ""),
    header = Header(0, "", "", ""),
    itemList = listOf(),
    author = Author(
        icon = "https://img.zcool.cn/community/01f05e5b9e6b0a6a49f0f7f0f0f0f0.png@1280w_1l_2o_100sh.png",
        name = "benyq",
        description = "benyq",
        latestReleaseTime = 0L
    ),
    playInfo = null
)

data class VideoState(
    val relateVideo: List<ItemData> = emptyList(),
    val relateVideoError: String = "",
    val replyType: ReplyType = ReplyType(VideoReply.LATEST),
): UiState


data class ReplyType(val type: VideoReply) {
    fun getTitle(): String {
        return if (type == VideoReply.LATEST) {
            "最新评论"
        } else {
            "最热评论"
        }
    }

    fun getTypeString(): String {
        return if (type == VideoReply.LATEST) {
            "按时间"
        } else {
            "按热度"
        }
    }
}
