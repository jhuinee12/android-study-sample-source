package com.example.ss210923_fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "channel";
    private String title, msg;

    /**
     * 토큰 생성 모니터링 :: 새 토큰이 생성될 때마다 onNewToken 콜백 호출
     * onNewToken이 호출되는 두 가지 시나리오가 있습니다.
     * 1) 최초 앱 시작 시 새로운 토큰이 생성되는 경우
     * 2) 기존 토큰이 변경될 때마다
     * #2에서는 기존 토큰이 변경되는 세 가지 시나리오가 있습니다.
     * A) 앱이 새 기기로 복원됩니다.
     * B) 사용자가 앱을 제거/재설치하는 경우
     * C) 사용자가 앱 데이터 삭제
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    /**
     * 타사 서버에 토큰을 유지합니다.
     * 이 방법을 수정하여 사용자의 FCM 등록 토큰을 임의의 응용 프로그램에서 유지 관리하는 서버 측 계정
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e(TAG,"onMessageReceived");

        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent :: notification에서 실질적으로 쓰임
        // 다른 애플리케이션의 권한을 허가해서 본인 앱에서 실행
        // notification을 눌렀을 때의 행위
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManagerCompat.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManagerCompat.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});


        notificationManagerCompat.notify(0, builder.build());
        builder.setContentIntent(contentIntent);
    }
}
