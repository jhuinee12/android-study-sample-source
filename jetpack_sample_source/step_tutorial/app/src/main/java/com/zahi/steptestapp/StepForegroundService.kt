package com.zahi.steptestapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class StepForegroundService: Service() {

    private val channelID = "STEP_COUNTER"
    private val channelName = "stepCounter"

    // 바인드를 허용
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel( channelID, channelName, NotificationManager.IMPORTANCE_HIGH )
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 서비스를 시작하게 함
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification().build())    // 0은 동작 안함. 0을 제외한 아무 숫자
        return START_NOT_STICKY
    }

    private fun createNotification(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, channelID)
            .setContentTitle("만보기 앱")
            .setContentText("만보기 측정 중...")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
    }

    fun updateNotification(stepCount: Int) {
        createNotification().setContentText("$stepCount 걸음")

        startForeground(1, createNotification().build())    // 0은 동작 안함. 0을 제외한 아무 숫자
    }
}