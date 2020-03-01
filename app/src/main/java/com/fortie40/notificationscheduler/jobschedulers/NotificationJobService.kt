package com.fortie40.notificationscheduler.jobschedulers

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import com.fortie40.notificationscheduler.R
import com.fortie40.notificationscheduler.helperclasses.ShowNotification

class NotificationJobService: JobService() {
    private lateinit var notifyManager: NotificationManager
    private lateinit var showNotification: ShowNotification

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        notifyManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        showNotification = ShowNotification(notifyManager, this)
        showNotification.createNotificationChannel()
        showNotification.sendNotifications(this.getString(R.string.job_started),
            this.getString(R.string.your_job_is_running))
        //return true
        return false
    }
}