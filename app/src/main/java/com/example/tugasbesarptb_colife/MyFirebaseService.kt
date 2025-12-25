package com.example.tugasbesarptb_colife

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

private const val CHANNEL_ID = "REMINDER_JATUH_TEMPO"

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "NEW TOKEN: $token")
        sendTokenToServer(token)
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        val title = msg.notification?.title ?: msg.data["title"] ?: "Pengingat"
        val body = msg.notification?.body ?: msg.data["body"] ?: "Ada tagihan jatuh tempo"

        
        val piutangId = msg.data["piutangId"] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("open", if (piutangId.isNotEmpty()) "piutang_detail" else "notification")
            putExtra("piutangId", piutangId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        sendNotification(title, body, intent)
    }

    private fun sendTokenToServer(token: String) {
        val sharedPreferences = getSharedPreferences("APP_PREF", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId == -1) return

        val url = "http://10.0.2.2:3000/api/fcm/save-fcm-token"

        val json = JSONObject().apply {
            put("userId", userId)
            put("fcmToken", token)
        }

        val client = OkHttpClient()
        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send token to server: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("FCM", "Token sent to server: $token")
                } else {
                    Log.e("FCM", "Failed to send token, response code: ${response.code}")
                }
            }
        })
    }

    private fun sendNotification(title: String, body: String, intent: Intent) {
        val manager = getSystemService(NotificationManager::class.java)

        if (manager != null) {
        
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Pengingat Hutang/Piutang",
                    NotificationManager.IMPORTANCE_HIGH
                )
                manager.createNotificationChannel(channel)
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val notif = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            manager.notify(System.currentTimeMillis().toInt(), notif)
        }
    }
}
