package com.android.startupwizard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView


class TimeActivity : AppCompatActivity() {
    private var dataView: LinearLayout? = null
    private var timeView: LinearLayout? = null
    private var timeZoneView: LinearLayout? = null
    private var date: TextView? = null
    private var time: TextView? = null
    private var timeZone: TextView? = null
    private var timeZoneTitle: TextView? = null
    private var next: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_time)
        dataView = findViewById(R.id.date_container)
        timeView = findViewById(R.id.time_container)
        timeZoneView = findViewById(R.id.timezone_container)
        date = findViewById(R.id.date)
        time = findViewById(R.id.time)
        timeZone = findViewById(R.id.timezone)
        timeZoneTitle = findViewById(R.id.timezone_title)
        next = findViewById(R.id.skip)
        next?.text = getString(R.string.next)
        dataView?.setOnClickListener { showDatePicker(this) }
        timeView?.setOnClickListener { showTimePicker(this) }
        timeZoneView?.setOnClickListener { showTimeZonePicker(this) }
        next?.setOnClickListener { startActivity(Intent(this, GesturesActivity::class.java)) }

        DateTimeData.timeZone.observe(this) { timeZone?.text = it }
        DateTimeData.timeZoneTitle.observe(this) { timeZoneTitle?.text = it }
        DateTimeData.date.observe(this) { date?.text = it }
        DateTimeData.time.observe(this) { time?.text = it }

        ActivityUtils.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        refreshCurrentState()
        updateStatusBar(2)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityUtils.removeActivity(this)
    }
}