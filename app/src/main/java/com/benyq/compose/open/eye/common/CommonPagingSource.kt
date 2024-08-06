package com.benyq.compose.open.eye.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
abstract class CommonPagingSource<T: Any, R: Any>: PagingSource<T, R>() {

    override fun getRefreshKey(state: PagingState<T, R>): T? = null

    override suspend fun load(params: LoadParams<T>): LoadResult<T, R> {
        return try {
            val pageKey: T? = params.key
            val response = request(pageKey)
            LoadResult.Page(
                response.data,
                params.key,
                response.nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    abstract suspend fun request(pageKey: T?): ReturnData<T, R>
}

data class ReturnData<T, R>(
    val data: List<R>,
    val nextKey: T?
)