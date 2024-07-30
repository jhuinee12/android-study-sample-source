package com.zahi.digital_photo_frame

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()

            var granted by remember { mutableStateOf(false) }
            val launcher =
                // 권한을 요청할 때는 rememberLauncherForActivityResult 를 사용한다.
                rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    granted = isGranted
                }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                granted = true
            }

            if (granted) { // 화면 띄우기
                viewModel.fetchPhotos()
                HomeScreen(viewModel.photoUris.value)
            } else { // 권한 요청 화면 띄우기
                PermissionRequestScreen {
                    // onClick 구현 -> 권한 요청
                    launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }
}

class MainViewModel(application: Application): AndroidViewModel(application) {
    // 사진을 ComposeUI로 제공하기 위한 state
    private val _photoUris = mutableStateOf(emptyList<Uri>())
    val photoUris: State<List<Uri>> = _photoUris

    // 사진을 가져오는 기능
    fun fetchPhotos() {
        val uris = mutableListOf<Uri>()

        getApplication<Application>().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
        )?.use { cursor -> // use를 사용하면 객체가 없을 때 자동 close
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex) // 몇번째 데이터 값인지를 받아와서 id로 사용

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id,
                )

                uris.add(contentUri)
            }
        }

        _photoUris.value = uris
    }
}

@Composable
fun PermissionRequestScreen(onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("권한이 허용되지 않았습니다")
        Button(onClick = onClick) { // onClick은 Callback 형식으로 구현
            Text("권한 요청")
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(photoUris: List<Uri>) {
    // 페이저 상태 저장
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 => 이미지
        HorizontalPager(count = photoUris.size, state = pagerState,
            modifier = Modifier.weight(1f).padding(16.dp).fillMaxSize()) { pageIndex ->
            Card(modifier = Modifier.graphicsLayer {
                /* 화면 넘기기 효과 !! (수학적인 계산) */
                val pageOffset = calculateCurrentOffsetForPage(pageIndex).absoluteValue
                lerp(0.85f, 1f, 1f-pageOffset.coerceIn(0f, 1f)).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(0.5f, 1f, 1f-pageOffset.coerceIn(0f,1f))
            }) {
                Image(
                    painter = rememberImagePainter(data = photoUris[pageIndex]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        // 하단 => indicator
        HorizontalPagerIndicator(pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop