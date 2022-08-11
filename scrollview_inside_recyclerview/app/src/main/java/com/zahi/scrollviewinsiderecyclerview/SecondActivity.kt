package com.zahi.scrollviewinsiderecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zahi.scrollviewinsiderecyclerview.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private var mBinding: ActivitySecondBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayList = arrayListOf<MainData>()

        for (i in 1..15) {
            arrayList.add(MainData(i, i.toString()))
        }

        binding.mainRecyclerView.adapter = MainAdapter(arrayList)
        binding.secondRecyclerView.adapter = SecondAdapter(arrayList)

        binding.btnPrev.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@SecondActivity, ThirdActivity::class.java))
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}