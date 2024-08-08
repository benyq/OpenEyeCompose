package com.benyq.compose.open.eye.base.common.mvi.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.compose.open.eye.base.common.mvi.ContainerLazy
import com.benyq.compose.open.eye.base.common.mvi.MutableContainer
import com.benyq.compose.open.eye.base.common.mvi.UiEvent
import com.benyq.compose.open.eye.base.common.mvi.UiState

/**
 * 构建viewModel的Ui容器，存储Ui状态和一次性事件
 */
fun <STATE : UiState, SINGLE_EVENT : UiEvent> ViewModel.containers(
    initialState: STATE,
): Lazy<MutableContainer<STATE, SINGLE_EVENT>> {
    return ContainerLazy(initialState, viewModelScope)
}