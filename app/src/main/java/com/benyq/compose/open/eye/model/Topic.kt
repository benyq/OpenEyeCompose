package com.benyq.compose.open.eye.model

class SpecialTopics : CommonResult<Topic>()

data class Topic(val data: TopicData)

data class TopicData(val id: Int, val image: String, val actionUrl: String)