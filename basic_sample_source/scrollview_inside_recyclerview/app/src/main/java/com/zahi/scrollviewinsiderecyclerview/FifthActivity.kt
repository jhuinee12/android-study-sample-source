package com.zahi.scrollviewinsiderecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zahi.scrollviewinsiderecyclerview.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {
    private var mBinding: ActivityFifthBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFifthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPrev.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}