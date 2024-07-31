package com.benyq.compose.open.eye

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.benyq.compose.open.eye.http.OpenEyeApi
import com.benyq.compose.open.eye.http.openEyeApi
import com.benyq.compose.open.eye.ui.theme.OpenEyeComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenEyeComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    Box {
        Text(
            text = "Hello $name!",
            modifier = modifier
                .align(Alignment.Center)
                .clickable {
                    coroutineScope.launch {
                        val daily = openEyeApi.getDaily()
                        Log.d("TAG", "Greeting: $daily")
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenEyeComposeTheme {
        Greeting("Android")
    }
}