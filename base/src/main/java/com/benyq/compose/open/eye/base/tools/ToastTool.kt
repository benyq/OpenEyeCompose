package com.benyq.compose.open.eye.base.tools

import android.content.Context
import android.widget.Toast
import com.benyq.compose.open.eye.base.common.appContext


fun showToast(text: String, context: Context = appContext) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}