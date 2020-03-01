package com.fortie40.notificationscheduler.jobs

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay

class Work {
    suspend fun doWork() {
        delay(10000)
    }

    suspend fun cancelJoinWork(job: Job) {
        job.cancelAndJoin()
    }
}