package com.fortie40.notificationscheduler.jobs

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay

class Work {
    suspend fun doWork() {
        for (i in 1..10) {
            delay(2000)
            println("Word $i")
        }
    }

    suspend fun cancelJoinWork(job: Job) {
        job.cancelAndJoin()
    }
}