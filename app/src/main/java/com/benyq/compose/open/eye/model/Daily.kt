package com.benyq.compose.open.eye.model

data class Daily(val issueList: List<Issue>, val nextPageUrl: String? = null) {

    operator fun plus(daily: Daily?): Daily {
        if (daily == null) return this
        return Daily(issueList + daily.issueList, daily.nextPageUrl)
    }

}


