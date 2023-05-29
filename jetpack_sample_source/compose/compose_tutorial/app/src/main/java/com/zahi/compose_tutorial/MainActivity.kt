package com.zahi.compose_tutorial

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zahi.compose_tutorial.ui.theme.Compose_tutorialTheme

class MainActivity : BaseActivity() {
    @Preview
    @Composable
    override fun SetupUI() {
        ThisTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column() {
                    Greeting("Android")
                    Greeting("Android")
                    MakeButton("gggg")
                }
            }
        }
    }

    @Composable
    fun ClickCounter(clicks:Int, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(text = "click!! $clicks time")
        }
    }

    @Composable
    private fun MakeButton(name: String) {
        Surface(
            Modifier.padding(vertical = 4.dp, horizontal = 8.dp), color = MaterialTheme.colors.primary
        ) {
            Row(modifier = Modifier.padding(24.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Hello, ")
                    Text(text = name)
                }
                OutlinedButton(
                    onClick = { /* TODO */ }
                ) {
                    Text("Show more")
                }
            }
        }
    }
}