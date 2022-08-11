package com.zahi.scrollviewinsiderecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zahi.scrollviewinsiderecyclerview.databinding.ActivityFourthBinding

class FourthActivity : AppCompatActivity() {
    private var mBinding: ActivityFourthBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPrev.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@FourthActivity, FifthActivity::class.java))
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}