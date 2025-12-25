package com.example.tugasbesarptb_colife

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color // <- import yang benar
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.random.Random

object NotificationHelper {
    private const val CHANNEL_ID = "piutang_channel"
    private const val CHANNEL_NAME = "Pengingat Piutang"
    private const val CHANNEL_DESCRIPTION = "Notifikasi pengingat piutang dan hutang"

    fun showNotification(context: Context, title: String, message: String, intent: Intent? = null) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat channel untuk Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = CHANNEL_DESCRIPTION
                    enableLights(true)
                    lightColor = Color.RED // <- gunakan android.graphics.Color
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 300, 200, 300)
                }
                manager.createNotificationChannel(channel)
            }
        }

        // Flags PendingIntent
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        // PendingIntent
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(context, Random.nextInt(0, Int.MAX_VALUE), it, flags)
        }

        // Build notifikasi
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(Color.RED) // <- warna merah, aman
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .apply { if (pendingIntent != null) setContentIntent(pendingIntent) }
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notif)
    }
}