package com.fortie40.notificationscheduler.jobschedulers

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import com.fortie40.notificationscheduler.R
import com.fortie40.notificationscheduler.helperclasses.ShowNotification
import com.fortie40.notificationscheduler.jobs.Work
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkJobService: JobService() {
    private lateinit var notifyManager: NotificationManager
    private lateinit var showNotification: ShowNotification
    private lateinit var work: Work
    private lateinit var job: Job

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        work = Work()
        notifyManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        showNotification = ShowNotification(notifyManager, this)
        showNotification.createNotificationChannel()
        showNotification.sendNotifications(getString(R.string.job_started),
            getString(R.string.your_job_is_running))

        // start the job
        startWork(p0)

        return true
    }

    private fun startWork(p0: JobParameters?) {
        job = CoroutineScope(IO).launch {
            work.doWork()
            withContext(Main) {
                jobFinished(p0, false)
                showNotification.sendNotifications(getString(R.string.job_has_finished),
                    getString(R.string.your_job_has_finished_running))
            }
        }
    }

    fun cancelJoinWork() {
        CoroutineScope(IO).launch {
            work.cancelJoinWork(job)
        }
    }
}