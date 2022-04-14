package com.example.ks20220302_googlefitapi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ks20220302_googlefitapi.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.snackbar.Snackbar


const val MAIN_TAG = "GoogleFitnessAPI"
const val multiplePermissionsCode = 100     // 퍼미션 응답 처리 코드

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    private val fitnessOptions: FitnessOptions by lazy {
        FitnessOptions.builder()
            .addDataType(DataType.TYPE_LOCATION_SAMPLE)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            /*.addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)*/
            .build()
    }
    private val runningQOrLater  = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissionsAndRun()

        binding.btnLiveLocation.setOnClickListener {  // 01. 센서 데이터 감지
            supportFragmentManager.beginTransaction().replace(R.id.layout_frame, SensorFragment()).commit()
        }

        binding.btnLiveStepCount.setOnClickListener {   // 02. 데이터 기록
            supportFragmentManager.beginTransaction().replace(R.id.layout_frame, RecordingFragment()).commit()
        }

        binding.btnWeight.setOnClickListener {   // 03. 과거 데이터
            supportFragmentManager.beginTransaction().replace(R.id.layout_frame, HistoryFragment()).commit()
        }
    }

    // <editor-fold desc = "1. 권한 설정">
    private fun checkPermissionsAndRun() {
        if (permissionApproved()) { // 안드로이드 10 이상 권한 부여 완료 || 안드로이드 9 이하
            fitSignIn()
        } else {                    // 안드로이드 10 이상 권한 부여 X
            requestRuntimePermissions()
        }
    }

    private fun permissionApproved(): Boolean {
        return if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            true
        }
    }

    private fun requestRuntimePermissions() {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

            if (shouldProvideRationale) {
                Log.i(SENSOR_TAG, "Displaying permission rationale to provide additional context.")
                Snackbar.make(
                    binding.mainActivityView,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACTIVITY_RECOGNITION),
                            multiplePermissionsCode)
                    }
                    .show()
            } else {
                Log.i(MAIN_TAG, "Requesting permission")
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACTIVITY_RECOGNITION),
                    multiplePermissionsCode)
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            grantResults.isEmpty() -> {
                Log.i(SENSOR_TAG, "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                // Permission was granted.
                fitSignIn()
            }
            else -> {
                Snackbar.make(
                    binding.mainActivityView,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.settings) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            "com.example.ks20220302_googlefitapi", null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc = "2. 계정 설정 // 테스트로 등록된 계정만 사용 가능">

    private fun fitSignIn() {
        if (oAuthPermissionsApproved()) { Log.i(MAIN_TAG, "Google Account True") }
        else {
                GoogleSignIn.requestPermissions(
                    this,
                    multiplePermissionsCode,
                    getGoogleAccount(), fitnessOptions)
        }
    }

    private fun oAuthPermissionsApproved() = GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> { Log.i(MAIN_TAG, "result OK") }
            else -> oAuthErrorMsg(requestCode, resultCode)
        }
    }

    private fun oAuthErrorMsg(requestCode: Int, resultCode: Int) {
        val message =   "구글 계정 정보 입력 실패\n" +
                        "Request code was: $requestCode\n" +
                        "Result code was: $resultCode"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.e(SENSOR_TAG, message)
    }
    // </editor-fold>
}