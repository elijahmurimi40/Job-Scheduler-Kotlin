package com.fortie40.notificationscheduler.jobschedulers

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import com.fortie40.notificationscheduler.jobs.ShowNotification

class NotificationJobService: JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        val notifyManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val showNotification = ShowNotification(notifyManager, this)
        showNotification.createNotificationChannel()
        showNotification.sendNotifications()
        //return true
        return false
    }
}