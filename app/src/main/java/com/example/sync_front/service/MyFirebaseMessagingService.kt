package com.example.sync_front.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.sync_front.ui.main.MainActivity
import com.example.sync_front.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.example.sync_front.BuildConfig.BASE_URL


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // 알림 채널 생성
        createNotificationChannel()


        // 제목과 메시지 추출
        val title = remoteMessage.notification?.title ?: "Default title"
        val message = remoteMessage.notification?.body ?: "Default message"

        // 타입에 따른 채널 ID 설정
        val channelId = when (title) {
            "채팅" -> "ChatChannel"
            "커뮤니티" -> "CommunityChannel"
            "공지" -> "OpenChatChannel"
            "일정" -> "RemindChannel"
            "후기" -> "ReviewChannel"
            else -> "OpenChatChannel"
        }

        // 알림 전송
        sendNotification(title, message, channelId)

    }

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("${BASE_URL}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

        // 서버에 토큰 업데이트 API 호출
        val call = apiService.updateToken(TokenUpdateRequest(token))
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(
                call: retrofit2.Call<Void>,
                response: retrofit2.Response<Void>
            ) {
                if (response.isSuccessful) {
                    Log.d("FCM", "Token updated successfully")
                } else {
                    Log.e("FCM", "Failed to update token")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("FCM", "Error updating token", t)
            }
        })
    }

    private fun createNotificationChannel() {
        // Android Oreo 이상에서는 알림 채널을 설정합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chatChannel = NotificationChannel(
                "ChatChannel",
                "Chat Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val communityChannel = NotificationChannel(
                "CommunityChannel",
                "Community Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val openChatChannel = NotificationChannel(
                "OpenChatChannel",
                "OpenChat Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val remindChannel = NotificationChannel(
                "RemindChannel",
                "Remind Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val reviewChannel = NotificationChannel(
                "ReviewChannel",
                "Review Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(chatChannel)
            notificationManager.createNotificationChannel(communityChannel)
            notificationManager.createNotificationChannel(openChatChannel)
            notificationManager.createNotificationChannel(remindChannel)
            notificationManager.createNotificationChannel(reviewChannel)
        }
    }

    private fun sendNotification(title: String, message: String, channelId: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 로그 추가: 채널 ID와 알림 정보를 로그로 기록
        Log.d(
            "FCMService",
            "Sending notification on channel: $channelId. Title: $title, Message: $message"
        )

        // 알림을 클릭했을 때 열릴 액티비티 설정
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // 알림 구성
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_sync_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // 알림 표시
        notificationManager.notify(0, notification)

        // 로그 추가: 알림이 표시된 후의 로그
        Log.d("FCMService", "Notification sent on channel: $channelId. ID: 0")
    }

}
