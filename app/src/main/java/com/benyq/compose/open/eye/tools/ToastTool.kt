package com.benyq.compose.open.eye.tools

import android.content.Context
import android.widget.Toast
import com.benyq.compose.open.eye.App


fun showToast(text: String, context: Context = App.CONTEXT) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}