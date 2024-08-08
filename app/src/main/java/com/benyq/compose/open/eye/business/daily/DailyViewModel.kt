package com.benyq.compose.open.eye.business.daily

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.benyq.compose.open.eye.base.common.BaseViewModel
import com.benyq.compose.open.eye.http.openEyeApi
import com.benyq.compose.open.eye.model.Item


class DailyViewModel : BaseViewModel() {


    val bannerList = mutableStateListOf<Item>()

    val pageFlow = Pager(config = PagingConfig(pageSize = 10,
        initialLoadSize = 10,
        prefetchDistance = 2), pagingSourceFactory = {
        DailyPagingSource(openEyeApi, bannerList)
    }).flow.cachedIn(viewModelScope)

}

