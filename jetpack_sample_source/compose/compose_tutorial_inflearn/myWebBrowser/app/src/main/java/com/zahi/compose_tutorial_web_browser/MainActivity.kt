package com.zahi.compose_tutorial_web_browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahi.compose_tutorial_web_browser.ui.theme.Compose_tutorial_web_browserTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            HomeScreen(viewModel)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current // 주소 입력이 되면 포커스를 없애서 키보드가 내려가도록 한다.

    val (inputUrl, setUrl) = rememberSaveable {
        mutableStateOf("https://www.google.com")
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold( // 앱바
        topBar = { // 상단 앱바
            TopAppBar(
                title = { Text(text = "나만의 웹 브라우저") },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon( imageVector = Icons.Default.ArrowBack, contentDescription = "back", tint = Color.White )
                    }
                    IconButton(onClick = { viewModel.redo() }) {
                        Icon( imageVector = Icons.Default.ArrowForward, contentDescription = "forward", tint = Color.White )
                    }
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column( modifier = Modifier
            .padding(16.dp)
            .fillMaxSize() ) {
            // textfield의 hint는 label로 제공됨
            OutlinedTextField(
                value = inputUrl,
                onValueChange = setUrl,
                label = { Text(text = "https://") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.url.value = inputUrl
                    focusManager.clearFocus()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyWebView(viewModel, scaffoldState)
        }
    }
}

@Composable
fun MyWebView(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    // SharedFlow를 사용하기 위해서는 CoroutineScope를 사용해야 한다.
    val scope = rememberCoroutineScope() // 이건 snackBar를 띄울 떄 오류 발생. 고로 아래 객체 사용!!
    val webview = rememberWebview()

    // key에 객체가 변경이 됐을 떄 block이 실행됨
    // sharedFlow 와 launchedEffect를 같이 활용하면 일회성 이벤트를 통제하기 좋다.
    LaunchedEffect(Unit) {
        viewModel.undoSharedFlow.collectLatest {
            if (webview.canGoBack()) webview.goBack()
            else scaffoldState.snackbarHostState.showSnackbar("더이상 뒤로 갈 수 없습니다.")
        }
    }
    LaunchedEffect(Unit) {
        viewModel.redoSharedFlow.collectLatest {
            if (webview.canGoForward()) webview.goForward()
            else scaffoldState.snackbarHostState.showSnackbar("더이상 앞으로 갈 수 없습니다.")
        }
    }

    // Compose 에서는 Webview를 제공하지 않음
    // 기존 Android View를 사용하는 코드 !!
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webview }, // 처음 화면에 표시해야하는 View 객체의 인스턴스 표시
        update = { webview -> // composition이 발생할 때 실행되는 부분 :: factory에서 만들어진 객체의 인스턴스를 전달받음
            webview.loadUrl(viewModel.url.value)

/*            scope.launch {
                viewModel.undoSharedFlow.collectLatest {
                    if (webview.canGoBack()) webview.goBack()
                    else scaffoldState.snackbarHostState.showSnackbar("더이상 뒤로 갈 수 없습니다.")
                }
            }

            scope.launch {
                viewModel.redoSharedFlow.collectLatest {
                    if (webview.canGoForward()) webview.goForward()
                    else scaffoldState.snackbarHostState.showSnackbar("더이상 앞으로 갈 수 없습니다.")
                }
            }*/
        }
    )
}

@Composable
fun rememberWebview() : WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient() // 이럴 지정해주지 않으면 별도 chrome 앱으로 실행이 된다.
            loadUrl("https://google.com")
        }
    }
    return webView
}