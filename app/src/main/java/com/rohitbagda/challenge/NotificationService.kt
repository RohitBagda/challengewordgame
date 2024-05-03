package com.rohitbagda.challenge

import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.R
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showTurnNotification() {
        val notification = NotificationCompat.Builder(context, "challenge_notification")
            .setContentTitle("Challenge Notification")
            .setContentText("A turn was played")
            .setSmallIcon(R.drawable.notification_bg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        Log.i(ContentValues.TAG, "Attempting to notify")
        notificationManager.notify(Random.nextInt(), notification)
    }
}