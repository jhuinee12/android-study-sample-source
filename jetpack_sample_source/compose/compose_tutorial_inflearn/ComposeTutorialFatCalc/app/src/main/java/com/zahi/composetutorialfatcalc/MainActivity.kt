package com.zahi.composetutorialfatcalc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zahi.composetutorialfatcalc.ui.theme.ComposeTutorialFatCalcTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<BmiViewModel>()
            val navController = rememberNavController()

            val bmi = viewModel.bmi.value // ViewModel의 State 값을 가지고 오게 함

            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    HomeScreen() { height, weight ->// callback
                        viewModel.bmiCalculate(height, weight)
                        navController.navigate("result")
                    }
                }
                composable(route = "result") {
                    ResultScreen(navController, bmi)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(onResultClicked: (Double, Double) -> Unit) {
    val (height, setHeight) = rememberSaveable { // rememberSaveable를 사용하면 화면이 회전해도 데이터 유지
        mutableStateOf("")
    }
    val (weight, setWeight) = rememberSaveable {
        mutableStateOf("")
    }

    Scaffold( // 앱바
        topBar = { // 상단 앱바
            TopAppBar(
                title = { Text("비만도 계산기") }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            OutlinedTextField( //EditText 추가
                value = height,
                onValueChange = setHeight,
                label = { Text("키") }, //기존에 Hint와 같음
                modifier = Modifier.fillMaxWidth(), // 괄호로 꽉 채움
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField( //EditText 추가
                value = weight,
                onValueChange = setWeight,
                label = { Text("몸무게") }, //기존에 Hint와 같음
                modifier = Modifier.fillMaxWidth(), // 괄호로 꽉 채움
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(8.dp)) //여백
            Button(
                onClick = {
                    if (height.isNotEmpty() && weight.isNotEmpty()) {
                        onResultClicked(height.toDouble(), weight.toDouble())
                    }
                },
                modifier = Modifier.align(Alignment.End), //오른쪽 끝으로 이동
            ) {
                Text("결과")
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(navController: NavController, bmi: Double) {
    val text = when {
        bmi >= 35 -> "고도 비만"
        bmi >= 30 -> "2단계 비만"
        bmi >= 25 -> "1단계 비만"
        bmi >= 23 -> "과체중"
        bmi >= 18.5 -> "정상"
        else -> "저체중"
    }

    val imageRes = when {
        bmi >= 23 -> R.drawable.over
        bmi >= 18.5 -> R.drawable.normal
        else -> R.drawable.less
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("비만도 계산기") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text, fontSize = 30.sp)
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(
                    color = Color.Black
                )
            )

        }
    }
}

class BmiViewModel: ViewModel() {
    private val _bmi = mutableStateOf(0.0)
    val bmi: State<Double> = _bmi

    fun bmiCalculate(height: Double, weight: Double) {
        _bmi.value = weight / (height / 100.0).pow(2.0) // bmi 구하는 공식
    }
}