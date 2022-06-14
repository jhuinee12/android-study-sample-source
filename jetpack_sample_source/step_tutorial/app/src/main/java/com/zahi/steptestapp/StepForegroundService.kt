package com.zahi.steptestapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class StepForegroundService: Service(), SensorEventListener {

    private val channelID = "STEP_COUNTER"
    private val channelName = "stepCounter"

    lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor

    private lateinit var notification: NotificationCompat.Builder

    // 바인드를 허용
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel( channelID, channelName, NotificationManager.IMPORTANCE_LOW )
            notificationManager.createNotificationChannel(channel)
            createNotification()
        }
    }

    // 서비스를 시작하게 함
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notification.setContentText("만보기 측정 중...")
        startForeground(1, notification.build())    // 0은 동작 안함. 0을 제외한 아무 숫자
        return START_NOT_STICKY
    }

    private fun createNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notification=  NotificationCompat.Builder(this, channelID)
            .setContentTitle("만보기 앱")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(pendingIntent)

        initialize()
    }

    fun updateNotification(stepCount: Int) {
        notification.setContentText("$stepCount 걸음")
        startForeground(1, notification.build())    // 0은 동작 안함. 0을 제외한 아무 숫자
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
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
//            binding.stepCount.text = event.values[0].toInt().toString()
            updateNotification(event.values[0].toInt())
        }
    }

    // 센서의 정확도가 변경되면 호출
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}