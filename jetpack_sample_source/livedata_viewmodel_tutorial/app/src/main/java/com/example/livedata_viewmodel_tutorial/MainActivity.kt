package com.example.livedata_viewmodel_tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStore

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val TAG = "MainActivity"
    }

    lateinit var myNumberViewModel: MyNumberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModelProvider deprecated
        //myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel)

        // myNumberViewModel의 currentValue(LiveData)를 관찰함
        // 관찰자 = this
        myNumberViewModel.currentValue.observe(this, Observer {
            Log.d(TAG, "MainActivity - myNumberViewModel - currentValue 라이브데이터 값 변경 : $it")
            findViewById<TextView>(R.id.number_textview).text = it.toString()
        })

        findViewById<Button>(R.id.plus_btn).setOnClickListener(this)
        findViewById<Button>(R.id.minus_btn).setOnClickListener(this)
    }

    // 클릭 이벤트
    override fun onClick(p0: View?) {
        val userInput = findViewById<EditText>(R.id.userinput_edittext).text.toString().toColorInt()

        // 뷰모델에 라이브데이터 값을 변경하는 메소드 실행
        when (p0!!.id) {
            R.id.plus_btn -> myNumberViewModel.updateValue(actionType = ActionType.PLUS, userInput)
            R.id.minus_btn -> myNumberViewModel.updateValue(actionType = ActionType.MINUS, userInput)
        }
    }
}