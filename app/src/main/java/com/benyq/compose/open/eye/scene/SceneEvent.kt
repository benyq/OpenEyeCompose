package com.benyq.compose.open.eye.scene

/**
 *
 * @author benyq
 * @date 4/16/2024
 *
 */
interface SceneEvent {

    interface Action {
        companion object {
            const val BACK = 101
            const val FULLSCREEN = 102
            const val MORE_ACTION = 103
        }
    }
}