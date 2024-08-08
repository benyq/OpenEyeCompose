package com.benyq.compose.open.eye.base.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.compose.open.eye.base.common.coroutine.Coroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel() {


    protected fun <T> execute(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        executeContext: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.() -> T,
    ): Coroutine<T> {
        return Coroutine.async(scope, context, start, executeContext, block)
    }

    protected fun <R> submit(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Deferred<R>,
    ): Coroutine<R> {
        return Coroutine.async(scope, context) { block().await() }
    }


    protected fun <T> executeFlow(request: suspend () -> T): Flow<DataState<T>> {
        return flow<DataState<T>> {
            val response = request()
            emit(DataState.Success(response))
        }.flowOn(Dispatchers.IO)
            .onStart { emit(DataState.Loading(true)) }
            .catch { emit(DataState.Error<T>(it)) }
            .onCompletion {
                emit(DataState.Loading(false))
            }
    }

}