package com.benyq.compose.open.eye.base.common.mvi.internal

import com.benyq.compose.open.eye.base.common.mvi.MutableContainer
import com.benyq.compose.open.eye.base.common.mvi.UiEvent
import com.benyq.compose.open.eye.base.common.mvi.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class RealContainer<STATE : UiState, SINGLE_EVENT : UiEvent>(
    initialState: STATE,
    private val parentScope: CoroutineScope,
) : MutableContainer<STATE, SINGLE_EVENT> {

    private val _internalStateFlow = MutableStateFlow(initialState)

    private val _internalSingleEventSharedFlow = MutableSharedFlow<SINGLE_EVENT>()

    override val uiStateFlow: StateFlow<STATE> = _internalStateFlow

    override val singleEventFlow: Flow<SINGLE_EVENT> = _internalSingleEventSharedFlow

    override fun updateState(action: STATE.() -> STATE) {
        _internalStateFlow.update { action(_internalStateFlow.value) }
    }

    override fun sendEvent(event: SINGLE_EVENT) {
        parentScope.launch {
            _internalSingleEventSharedFlow.emit(event)
        }
    }

}