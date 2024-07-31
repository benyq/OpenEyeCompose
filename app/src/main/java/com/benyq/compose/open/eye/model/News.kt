package com.benyq.compose.open.eye.model

class News : CommonResult<NewsItem>()

data class NewsItem(val type: String, val data: NewsItemData)

data class NewsItemData(
    val text: String = "", val titleList: List<String>? = null,
    val backgroundImage: String = "", val actionUrl: String = "",
)