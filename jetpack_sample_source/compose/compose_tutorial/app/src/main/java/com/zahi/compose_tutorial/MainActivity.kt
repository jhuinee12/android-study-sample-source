package com.zahi.compose_tutorial

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : BaseActivity() {
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
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    ImageCard( Modifier
                        .fillMaxWidth(0.5f) // 절반
                        .padding(16.dp),
                        isFavorite ) { isFavorite = it } // callback
                    Greeting("Android")
                    Greeting("Android")
                    MakeButton("gggg")
                    MakeBox()
                    MakeForColumn()
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
}