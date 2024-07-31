package com.benyq.compose.open.eye.model

data class Daily(val issueList: MutableList<Issue>, val nextPageUrl: String? = null)