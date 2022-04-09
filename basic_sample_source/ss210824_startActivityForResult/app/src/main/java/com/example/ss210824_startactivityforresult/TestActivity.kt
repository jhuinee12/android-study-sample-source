package com.example.ss210824_startactivityforresult

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ss210824_startactivityforresult.databinding.ActivityMainBinding
import com.example.ss210824_startactivityforresult.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    private var mBinding: ActivityTestBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtBack.setOnClickListener {
            backActivity()
        }
    }

    private fun backActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}