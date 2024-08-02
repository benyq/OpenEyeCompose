package com.benyq.compose.open.eye.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}

fun Modifier.noRippleClick(onClick:()->Unit):Modifier {
    return this.clickable(
        interactionSource = NoRippleInteractionSource(),
        indication = null,
        onClick = onClick
    )
}

