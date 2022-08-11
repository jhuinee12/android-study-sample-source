package com.zahi.scrollviewinsiderecyclerview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zahi.scrollviewinsiderecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayList = arrayListOf<MainData>()

        for (i in 1..30) {
            arrayList.add(MainData(i, i.toString()))
        }

        binding.recyclerView.adapter = MainAdapter(arrayList)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}