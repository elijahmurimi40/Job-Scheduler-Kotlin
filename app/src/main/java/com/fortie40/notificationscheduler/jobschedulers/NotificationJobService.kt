package com.fortie40.notificationscheduler.jobschedulers

import android.app.job.JobParameters
import android.app.job.JobService

class NotificationJobService: JobService() {

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        return false
    }
}