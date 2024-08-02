package com.benyq.compose.open.eye.business.daily

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.benyq.compose.open.eye.http.OpenEyeApi
import com.benyq.compose.open.eye.model.Daily
import com.benyq.compose.open.eye.model.Item
import kotlinx.coroutines.delay

class DailyPagingSource(
    private val openEyeApi: OpenEyeApi,
    private val bannerList: MutableList<Item>
) : PagingSource<String, Item>() {
    override fun getRefreshKey(state: PagingState<String, Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Item> {
        return try {
            val pageKey: String? = params.key
            var nextKey: String? = null
            val nextPageUrl: String?
            val daily = if (pageKey.isNullOrEmpty()) {
                val d1 = openEyeApi.getDaily()
                var d2: Daily? = null
                if (d1.nextPageUrl != null) {
                    d2 = openEyeApi.getDaily(d1.nextPageUrl)
                }
                d1 + d2
            } else {
                openEyeApi.getDaily(pageKey)
            }
            nextPageUrl = daily.nextPageUrl
            val itemList = daily.issueList[0].itemList
            itemList.removeAll {
                it.type == "banner2"
            }
            if (pageKey.isNullOrEmpty()) {
                bannerList.clear()
                bannerList.addAll(itemList)
            }
            if (!nextPageUrl.isNullOrEmpty()) {
                nextKey = nextPageUrl
            }
            LoadResult.Page(
                if (pageKey.isNullOrEmpty()) listOf(Item()) else itemList,
                pageKey,
                nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}