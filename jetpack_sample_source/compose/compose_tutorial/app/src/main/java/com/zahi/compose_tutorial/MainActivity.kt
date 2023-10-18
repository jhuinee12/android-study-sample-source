package com.zahi.compose_tutorial

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
//    private val viewModel by viewModels<MainViewModel>()

    @Preview
    @Composable
    override fun SetupUI() {
//        val isFavorite = remember {
//            mutableStateOf(false)
//        } // 이렇게 쓸 때는 isFavorite.value 로 사용 !
        var isFavorite by remember {
            mutableStateOf(false)
        } // boolean 값 상태를 저장함. remember가 저장 -> state가 변경되면 ui가 다시 그려짐

        ThisTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    MakeNavigation()
                    MakeScaffold()
                    ImageCard(
                        Modifier
                            .fillMaxWidth(0.5f) // 절반
                            .padding(16.dp),
                        isFavorite ) { isFavorite = it } // callback
                    Greeting("Android")
                    Greeting("Android")
                    MakeButton("gggg")
                    MakeBox()
//                    MakeForColumn()
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
            Modifier
                .height(100.dp)
                .width(300.dp)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            color = MaterialTheme.colors.primary
        ) {
            // Row : LinearLayout Horizontal
            // Column : LinearLayout Vertical
            Row(modifier = Modifier.padding(24.dp)) {
                Column(
                    // 차례대로 실행되기 때문에 간혹가다 순서의 영향이 있을 수 있음
                    modifier = Modifier
                        .background(color = Color.Blue),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Hello, ")
                    Spacer(Modifier.height(16.dp)) // Spacer : Margin
                    Text(text = name)
                }
                Spacer(Modifier.width(16.dp)) // Spacer : Margin
                OutlinedButton(
                    onClick = { /* TODO */ }
                ) {
                    Text("Show more")
                }
            }
        }
    }

    @Composable
    fun MakeBox() {
        Spacer(modifier = Modifier.height(30.dp))
        // Box는 FrameLayout과 비슷한 느낌
        Box(modifier = Modifier
            .background(color = Color.Green)
            .fillMaxWidth()
            .height(100.dp)
//            , contentAlignment = Alignment.TopEnd  // Box의 정렬 조건
        ) {
            Text("Hello")
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            , contentAlignment = Alignment.BottomEnd) {
                Text("!!")
            }
        }
    }
    
    @Composable
    fun MakeForColumn() {
        Spacer(modifier = Modifier.height(20.dp))

//        Column(
//            modifier = Modifier
//                .background(Color.Cyan)
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
//        ) {
//            for (i in 1..20) {
//                Text("글씨 $i")
//            }
//        }

        // LazyColumn으로 간단한 RecyclerView 구현 가능
        LazyColumn(
            Modifier
                .background(Color.Cyan)
                .fillMaxWidth(),
            contentPadding =  PaddingValues(16.dp), // 사방의 padding 지정
            verticalArrangement = Arrangement.spacedBy(10.dp) // 아이템 간의 space 조절
        ) {
            item {
                Text("Header")
            }
            items(50) {
                Text("글씨 $it")
            }
            item {
                Text(text = "Footer")
            }
        }
    }

    @Composable
    fun ImageCard(modifier: Modifier, isFavorite: Boolean, onTabFavoriteCallback: (Boolean) -> Unit) {
        // CardView와 동일한 기능
        Card(
            shape = RoundedCornerShape(8.dp), // 모서리 둥글게
            elevation = 5.dp,
            modifier = modifier
        ) {
            Box(
                modifier = Modifier.height(200.dp)
            ) {
                // ImageView와 동일
                // painter: 이미지 파일 표시
                Image(
                    painter = painterResource(id = R.drawable.zzanggu),
                    contentDescription = "짱구야 놀자",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        onTabFavoriteCallback(!isFavorite)
                    }) {
                        Icon(
//                            imageVector = Icons.Default.FavoriteBorder,
                            imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorites",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MakeScaffold() {
//        val textValue = remember {
//            mutableStateOf("")
//        }

        // MutableState의 값을 변경하면 화면이 다시 그려진다
        // recomposition

        val text1: MutableState<String> = remember {
            // text1을 수정하고 싶으면 MutableState안에 String으로 접근해야 함
            // text.value = "" 로 사용
            mutableStateOf("Test 1")
        }
        val text2: String by remember {
            // text2와 text3는 같은 효과
            // by를 사용하게 되면 text2를 변경하고 싶을 때
            // text2 = "" 라고 쓰면 된다.
            mutableStateOf("Test 2")
        }
        val (text3: String, setValue: (String) -> Unit) = remember {
            // text3를 수정하고 싶으면 setValue를 사용
            mutableStateOf("")
        }

        // scaffold는 Material Application에서 사용되는 Composable Function
        // TopBar, BottomBar, SnackBar, FloatingActionButton, Drawer 등 사용 가능
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope() // 코루틴 스코프
        val keyboardController = LocalSoftwareKeyboardController.current

        Scaffold(
            scaffoldState = scaffoldState,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField( // EditText
                    value = text3,
                    onValueChange = setValue,
                )
                Button(
                    onClick = {
                        keyboardController?.hide()
                        scope.launch {
                            // toast 느낌
                            scaffoldState.snackbarHostState.showSnackbar("스낵바 띄우기 $text3")
                        }
                    }
                ) {
                    Text("클릭!!")
                }
            }
        }
    }

    @Composable
    fun MakeNavigation() {
        val navController = rememberNavController()
        val viewModel = viewModel<MainViewModel>()

        NavHost(navController = navController, startDestination = "first") {
            composable("first") {
                FirstScreen(navController)
            }
            composable("second") {
                SecondScreen(navController)
            }
            composable("third/{value}") {
                viewModel.ChangeValue(it.arguments?.getString("value") ?: "")
                ThirdScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun FirstScreen(navController: NavController) {
    val (value, setValue) = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "첫 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("second")
        }) {
            Text(text = "두번째!!")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = value, onValueChange = setValue)
        Button(onClick = {
            if (value.isNotEmpty()) {
                navController.navigate("third/$value") // third에 value 값을 넘겨줌
            }
        }) {
            Text(text = "세번째!!")
        }
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "두번째 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigateUp()
        }) {
            Text(text = "뒤로가기")
        }
    }
}

@Composable
fun ThirdScreen(navController: NavController, viewModel: MainViewModel) { // textField 값이 들어오도록!
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "세번째 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = viewModel.data.value)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "뒤로가기")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.ChangeValue("world")
        }) {
            Text(text = "텍스트 변경")
        }
    }
}