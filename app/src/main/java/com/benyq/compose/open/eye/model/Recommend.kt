package com.benyq.compose.open.eye.model

class Recommend : CommonResult<RecommendItem>()

data class RecommendItem(val type: String, val data: RecommendItemData)

data class RecommendItemData(val content: Item)