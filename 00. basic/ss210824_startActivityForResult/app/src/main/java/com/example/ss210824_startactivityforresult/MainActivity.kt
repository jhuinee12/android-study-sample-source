package com.example.ss210824_startactivityforresult

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.ss210824_startactivityforresult.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    private val REQUEST_CODE = 1102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtNext.setOnClickListener {
            moveActivity2()
        }
    }

    /**
     * deprecated 된 방법
     */
    private fun moveActivity() {
        var intent = Intent(this, TestActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            Toast.makeText(this, "requestCode == REQUEST_CODE", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * callback을 만들어서 사용
     */
    private fun moveActivity2() {
        var intent = Intent(this, TestActivity::class.java)
        startActivityResult.launch(intent)
    }
    private var startActivityResult = registerForActivityResult(
            StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "MainActivity로 돌아왔다.", Toast.LENGTH_LONG).show()
        }
    }
}