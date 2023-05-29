package com.zahi.compose_tutorial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

abstract class BaseActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent { SetupUI() }
	}

	override fun onDestroy() {
		if (isTaskRoot && isFinishing) {
			finishAfterTransition()
		}
		super.onDestroy()
	}

	@Preview
	@Composable
	protected abstract fun SetupUI()

	@Composable
	fun ShowToast(message: String) {
		LaunchedEffect(true) {
			Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
		}
	}

	@Composable
	fun ThisTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
		val colors = if (darkTheme) {
			darkColors()
		} else {
			lightColors()
		}

		MaterialTheme(
			colors = colors,
			typography = typography,
			shapes = shapes,
			content = content
		)
	}

	@Composable
	fun Greeting(name: String) {
		Text(text = "Hello $name!")
	}

	private fun lightColors(): Colors {
		return lightColors(
			primary = Color(0xFF6200EE),
			primaryVariant = Color(0xFF3700B3),
			secondary = Color(0xFF03DAC6),
			secondaryVariant = Color(0xFF018786),
			background = Color.White,
			surface = Color.White,
			onPrimary = Color.White,
			onSecondary = Color.Black,
			onBackground = Color.Black,
			onSurface = Color.Black
		)
	}

	private fun darkColors(): Colors {
		return darkColors(
			primary = Color(0xFFBB86FC),
			primaryVariant = Color(0xFF3700B3),
			secondary = Color(0xFF03DAC6),
			background = Color(0xFF121212),
			surface = Color(0xFF121212),
			onPrimary = Color.Black,
			onSecondary = Color.Black,
			onBackground = Color.White,
			onSurface = Color.White
		)
	}
}
