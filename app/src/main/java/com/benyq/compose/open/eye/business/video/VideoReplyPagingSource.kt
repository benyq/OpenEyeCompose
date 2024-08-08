package com.benyq.compose.open.eye.business.video

import com.benyq.compose.open.eye.common.CommonPagingSource
import com.benyq.compose.open.eye.common.ReturnData
import com.benyq.compose.open.eye.http.OpenEyeApi
import com.benyq.compose.open.eye.model.ReplyData

class VideoReplyPagingSource(private val openEyeApi: OpenEyeApi, private val type: VideoReply, private val param: ()->Int): CommonPagingSource<Int, ReplyData>() {

    override suspend fun request(pageKey: Int?): ReturnData<Int, ReplyData> {
        val reply = if (type == VideoReply.HOT) {
            openEyeApi.getHotVideoReply(videoId = param(), lastId = pageKey)
        }else openEyeApi.getVideoReply(videoId = param(), lastId = pageKey)

        val data = reply.itemList.filter { it.type == "reply"  }.map { it.data }
        return ReturnData(
            data,
            if (data.isEmpty()) null else data.last().sequence
        )
    }

}

enum class VideoReply {
    HOT,
    LATEST
}