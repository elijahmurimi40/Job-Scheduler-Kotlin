package com.fortie40.notificationscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fortie40.notificationscheduler.jobschedulers.WorkJobService
import com.google.android.material.snackbar.Snackbar
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

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 > 0) {
                    seek_bar_progress.text = getString(R.string.seconds, p1.toString())
                } else {
                    seek_bar_progress.text = getString(R.string.not_set)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun scheduleJob() {
        mScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val selectedNetworkId = networkOptions.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
        val seekBarInteger = seek_bar.progress
        val seekBarSet = seekBarInteger > 0

        when(selectedNetworkId) {
            R.id.no_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.any_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.wifi_network -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }

        val serviceName = ComponentName(packageName, WorkJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(selectedNetworkOption)
            .setRequiresDeviceIdle(idle_switch.isChecked)
            .setRequiresCharging(charging_switch.isChecked)

        val constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE ||
                charging_switch.isChecked || idle_switch.isChecked || seekBarSet

        if (seekBarSet) {
            builder.setOverrideDeadline(seekBarInteger.toLong() * 1000)
        }

        if (constraintSet) {
            val myJobInfo = builder.build()
            mScheduler!!.schedule(myJobInfo)
            Snackbar.make(cancel_job, getString(R.string.job_scheduled), Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(cancel_job, getString(R.string.set_constraint), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun cancelJob() {
        if (mScheduler != null) {
            mScheduler!!.cancelAll()
            mScheduler = null
            Snackbar.make(cancel_job, getString(R.string.jobs_cancelled), Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(cancel_job, getString(R.string.no_jobs), Snackbar.LENGTH_SHORT).show()
        }
    }
}
