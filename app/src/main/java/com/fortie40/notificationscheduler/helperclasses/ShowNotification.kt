package com.fortie40.notificationscheduler.helperclasses

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.fortie40.notificationscheduler.MainActivity
import com.fortie40.notificationscheduler.R

class ShowNotification() {
    companion object {
        const val PRIMARY_CHANNEL_ID =
            "com.fortie40.notificationscheduler.PRIMARY_NOTIFICATION_CHANNEL"
        const val NOTIFICATION_ID = 0
    }

    private lateinit var mNotifyManager: NotificationManager
    private lateinit var mContext: Context

    constructor(notifyManager: NotificationManager, context: Context): this() {
        mNotifyManager = notifyManager
        mContext = context
    }

    fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(PRIMARY_CHANNEL_ID,
                mContext.getString(R.string.job_service_notification),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = mContext.getString(R.string.notifications_from_job_service)
            }

            mNotifyManager.createNotificationChannel(channel)
        }
    }

    private fun getNotificationBuilder(title: String, text: String): NotificationCompat.Builder {
        val notificationIntent = Intent(mContext, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(mContext,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(mContext, PRIMARY_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_job_running)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
    }

    fun sendNotifications(title: String, text: String) {
        val notifyBuilder = getNotificationBuilder(title, text)
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }
}