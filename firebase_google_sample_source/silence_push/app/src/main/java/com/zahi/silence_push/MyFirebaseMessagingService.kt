package com.zahi.silence_push

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val getData = remoteMessage.data
        val title   = getData["title"].toString()
        val msg     = getData["body"].toString()
        val type    = getData["type"].toString()
        val intent  = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        makeBuilder(title, msg, type)
    }

    private fun makeBuilder(title: String, msg: String, type: String) {
        val intent = Intent(baseContext, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val contentIntent: PendingIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        val builder: NotificationCompat.Builder 
            = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(applicationContext, "push")
            } else {
                NotificationCompat.Builder(applicationContext)
            }

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(500,500))

        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setContentIntent(contentIntent)

        if (type != "silence") {
            builder.setContentIntent(contentIntent)
            val now = Date()
            val id: Int = SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now).toInt()
            notificationManagerCompat.notify(id, builder.build())
        }
    }
}