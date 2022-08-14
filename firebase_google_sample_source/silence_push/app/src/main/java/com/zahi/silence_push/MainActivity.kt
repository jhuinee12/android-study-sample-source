package com.zahi.silence_push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import com.zahi.silence_push.databinding.ActivityMainBinding


/**
 * post
 *  https://fcm.googleapis.com/fcm/send
 *
 * header
 *  "Content-Type" : "application/json"
 *  "Authorization" : "key=AAAAzvqJu1k:APA91bFSfl-Ho00qbdJQJGu1yx62rpGcsCL5tDqMGY2Iy95fUBXUuxUJxSu02VQoaJNZzQk2VgDAU6ptgBMcF0Uf4U9cp1I5pHMsS_FfgdqdpZynafKdF7OP6mSuoQRYTaj4qfLgFvJq"
 *
 * body
 * {
 *   "to":"token",
 *   "priority" : "high",
 *   "data" : {
 *           "title" : "푸시 테스트",
 *           "message" : "푸시 테스트 중입니다.",
 *           "type" : "silence"
 *       }
 *   }
 *
 * response -error
 * {
 *      "multicast_id": 8980693848494960884,
 *      "success": 0,
 *      "failure": 1,
 *      "canonical_ids": 0,
 *      "results": [
 *          {
 *              "error": "NotRegistered"
 *          }
 *      ]
 * }
 *
 * response -success
 *{
 *      "multicast_id": 6800505770436478735,
 *      "success": 1,
 *      "failure": 0,
 *      "canonical_ids": 0,
 *      "results": [
 *          {
 *              "message_id": "0:1660469120609386%fa1d3a91f9fd7ecd"
 *          }
 *      ]
 * }
 */
class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createChannel()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            val token = task.result
            Log.e("파이어베이스 토큰", "onCreate: $token")
        })
    }

    fun createChannel() {
        val mNotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "push"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_LOW
            )
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}