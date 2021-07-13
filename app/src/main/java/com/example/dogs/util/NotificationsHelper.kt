package com.example.dogs.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dogs.R
import com.example.dogs.view.MainActivity

class NotificationsHelper(val context: Context) {
    private val CHANNEL_ID = "My channel ID"
    private val NOTIFICATION_ID = 42

    fun createNotification() {
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).run {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.dog_icon)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setNotificationSilent()
            .setSmallIcon(R.drawable.dog_icon)
            .setLargeIcon(largeIcon)
            .setContentTitle("Content title")
            .setContentText("Content text")
            .setContentInfo("Content info")
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null)
            )
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .build()

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is a nice channel, I promise"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}