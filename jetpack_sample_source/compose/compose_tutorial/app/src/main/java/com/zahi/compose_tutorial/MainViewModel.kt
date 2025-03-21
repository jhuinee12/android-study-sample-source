package com.zahi.compose_tutorial

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// ViewModel은 Activity와 LifeCycle이 동일하므로 remember를 신경쓰지 않아도 됨.
class MainViewModel: ViewModel() {
    private val _data =  mutableStateOf("")
    val data: State<String> = _data // 읽기 전용

    fun ChangeValue(value: String) {
        _data.value = value
    }
}