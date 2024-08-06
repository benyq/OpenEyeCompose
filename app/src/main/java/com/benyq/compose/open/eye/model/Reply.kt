package com.benyq.compose.open.eye.model


class Reply : CommonResult<ReplyItem>()

data class ReplyItem(
    val type: String,
    val data: ReplyData
)


data class ReplyData(
    val actionUrl: String,
    val cover: String?,
    val createTime: Long,
    val dataType: String,
    val hot: Boolean,
    val id: Long,
    val imageUrl: String,
    val likeCount: Int,
    val liked: Boolean,
    val message: String,
    val parentReply: ReplyData?,
    val parentReplyId: Long,
    val recommendLevel: String,
    val replyStatus: String,
    val rootReplyId: Long,
    val sequence: Int,
    val showConversationButton: Boolean,
    val showParentReply: Boolean,
    val sid: String,
    val type: String,
    val ugcVideoId: Long?,
    val ugcVideoUrl: String?,
    val user: User,
    val userBlocked: Boolean,
    val userType: String?,
    val videoId: Long,
    val videoTitle: String
)

data class User(
    val actionUrl: String,
    val area: String?,
    val avatar: String,
    val birthday: Long,
    val city: String?,
    val country: String,
    val cover: String,
    val description: String,
    val expert: Boolean,
    val followed: Boolean,
    val gender: String,
    val ifPgc: Boolean,
    val job: String,
    val library: String,
    val limitVideoOpen: Boolean,
    val nickname: String,
    val registDate: Long,
    val releaseDate: Long,
    val uid: Int,
    val university: String,
    val userType: String
)