package com.zahi.steptestapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.zahi.steptestapp.databinding.ActivityMainBinding
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor

    private val service = StepForegroundService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
    }

//    override fun onResume() {
//        super.onResume()
//        CoroutineScope(Dispatchers.Main).launch {
//            binding.stepCount.text = App.getInstance().getDataStore().step.first().toString()
//        }
//    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            binding.stepCount.text = event.values[0].toInt().toString()
        }
    }

    // 센서의 정확도가 변경되면 호출
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        sensorManager.unregisterListener(this)
//    }

    @SuppressLint("AutoDispose")
    private fun checkPermission() {
        RxPermissions(this)
            .request(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
            .subscribe {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(this)) {
                    onObtainingPermissionOverlayWindow()
                } else {
                    initialize()
                }
            }
    }

    private fun initialize() {
        // 센서 연결
        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // 센서 속도 설정
        // SENSOR_DELAY_FASTEST : 0ms 최대한 빠르게
        // SENSOR_DELAY_GAME : 20,000ms 게임에 적합한 속도
        // SENSOR_DELAY_UI : 60,000ms UI 수정에 적합한 속도
        // SENSOR_DELAY_NORMAL : 200,000ms 화면 방향 변화를 모니터링하기에 적합한 속도
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)

        startService()
    }

    // 다른앱 위에 그리기 설정 페이지로 이동
    private fun onObtainingPermissionOverlayWindow() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", this.packageName, null)
        permissionActivityResult.launch(intent)
    }

    private var permissionActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.e("TAG", "permissionActivityResult: $result")
        initialize()
    }   // callback

    private fun startService() {
        val intent = Intent(this.applicationContext, service::class.java)
        startService(intent)
    }
}