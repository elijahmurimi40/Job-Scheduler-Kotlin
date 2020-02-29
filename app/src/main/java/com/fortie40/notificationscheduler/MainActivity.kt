package com.fortie40.notificationscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fortie40.notificationscheduler.jobschedulers.NotificationJobService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val JOB_ID = 0
    }

    private var mScheduler: JobScheduler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        schedule_job.setOnClickListener {
            scheduleJob()
        }

        cancel_job.setOnClickListener {
            cancelJob()
        }
    }

    private fun scheduleJob() {
        mScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val selectedNetworkId = networkOptions.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE

        when(selectedNetworkId) {
            R.id.no_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.any_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.wifi_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }

        val serviceName = ComponentName(packageName, NotificationJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(selectedNetworkOption)
            .setRequiresDeviceIdle(idle_switch.isChecked)
            .setRequiresCharging(charging_switch.isChecked)

        val constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE ||
                charging_switch.isChecked || idle_switch.isChecked

        if (constraintSet) {
            val myJobInfo = builder.build()
            mScheduler!!.schedule(myJobInfo)
            Toast.makeText(this, getString(R.string.job_scheduled),
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.set_constraint),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelJob() {
        if (mScheduler != null) {
            mScheduler!!.cancelAll()
            mScheduler = null
            Toast.makeText(this, getString(R.string.jobs_cancelled), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.no_jobs), Toast.LENGTH_SHORT).show()
        }
    }
}
