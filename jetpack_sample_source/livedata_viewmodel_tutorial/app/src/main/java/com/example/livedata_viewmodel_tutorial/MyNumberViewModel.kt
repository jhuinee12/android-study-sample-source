package com.example.livedata_viewmodel_tutorial

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class ActionType {
    PLUS, MINUS
}

// 데이터의 변경
// 뷰모델은 데이터의 변경사항을 알려주는 라이브 데이터를 가지고 있고
class MyNumberViewModel : ViewModel() {

    companion object {
        private const val TAG = "MyNumberViewModel"
    }

    // Mutable LiveData => 수정 가능함
    // LiveData => 수정 불가능함

    // mutable을 사용할 때, 변수명은 언더바로 시작
    private val _currentValue = MutableLiveData<Int>()

    // 변경되지 않는 데이터를 가져올 때 이름은 _언더바 없이 설정
    // 공개적으로 가져오는 변수는 private이 아닌 public으로 외부에서도 접근가능하도록 설정
    // 하지만 값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정
    val currentValue: LiveData<Int> get() = _currentValue
    
    // 초기값 설정
    init {
        Log.d(TAG, "MyNumberViewModel - 생성자 호출")
        _currentValue.value = 0     // LiveData의 값 접근 방법: 변수명.value
    }

    fun updateValue(actionType: ActionType, input: Int) {
         when (actionType) {
             ActionType.PLUS    -> _currentValue.value = _currentValue.value?.plus(input)   // input만큼 더해줌
             ActionType.MINUS   -> _currentValue.value = _currentValue.value?.minus(input)  // input만큼 빼줌
         }
    }
}