package com.android.startupwizard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.TimeZoneNames
import android.icu.util.TimeZone
import android.icu.util.ULocale
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.startupwizard.App.Companion.mContext
import java.util.Calendar


object DateTimeData : ViewModel() {
    val date = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val timeZone = MutableLiveData<String>()
    val timeZoneTitle = MutableLiveData<String>()
}

fun refreshCurrentState() {
    val cal = Calendar.getInstance()
    DateTimeData.date.value = DateFormat.getLongDateFormat(mContext).format(cal.time)
    DateTimeData.time.value = DateFormat.getTimeFormat(mContext).format(cal.time)
    DateTimeData.timeZone.value = ZoneGetter.getZoneInfo(TimeZone.getDefault().id).displayName
    DateTimeData.timeZoneTitle.value =
        ZoneGetter.getZoneInfo(TimeZone.getDefault().id).standardName.split(" ")[0]
}

fun showDatePicker(context: Activity) {
    val cal = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth -> setDate(year, month, dayOfMonth) },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Activity) {
    val cal = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, min -> setTime(hourOfDay, min) },
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        DateFormat.is24HourFormat(mContext)
    ).show()
}

fun showTimeZonePicker(context: Activity) {
    val zones = ZoneGetter.getZonesList()
    AlertDialog.Builder(context)
        .setAdapter(ZoneGetter.getAdapter(context, zones)) { _, which ->
            Log.d("TAG", "showTimeZonePicker: ${zones[which]}")
            setTimeZone(zones[which].id)
        }
        .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        .create().show()
}

private fun setTime(hourOfDay: Int, min: Int) {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
    cal.set(Calendar.MINUTE, min)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    setMillis(cal.timeInMillis)
}

private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    setMillis(cal.timeInMillis)
}

private fun setMillis(millis: Long) {
    getAlarmManager().setTime(millis)
    refreshCurrentState()
}

private fun setTimeZone(zoneId: String) {
    getAlarmManager().setTimeZone(zoneId)
    refreshCurrentState()
}

private fun getAlarmManager(): AlarmManager {
    return mContext?.getSystemService(AlarmManager::class.java)!!
}


private object ZoneGetter {
    data class ZoneInfo(
        val id: String,
        val displayName: String,
        val standardName: String,
    )

    fun getZonesList(): List<ZoneInfo> {
        val zoneInfos = mutableListOf<ZoneInfo>()
        val zoneIds = mContext?.resources?.getStringArray(R.array.time_zones)
        val zoneNames = getZoneNamesInstance()
        zoneIds?.forEach { zoneInfos.add(getZoneInfo(it, zoneNames)) }
        return zoneInfos
    }

    fun getZoneInfo(zoneId: String): ZoneInfo {
        return getZoneInfo(zoneId, getZoneNamesInstance())
    }

    fun getZoneInfo(zoneId: String, zoneNames: TimeZoneNames): ZoneInfo {
        val timeZone = TimeZone.getTimeZone(zoneId)
        val displayName =
            zoneNames.getExemplarLocationName(TimeZone.getCanonicalID(zoneId) ?: zoneId)
                ?: timeZone.displayName
        val longGmt = timeZone.getDisplayName(false, TimeZone.LONG_GMT)
        val longStandard = timeZone.getDisplayName(false, TimeZone.LONG)
        val standardName = "$longGmt $longStandard"
        return ZoneInfo(zoneId, displayName, standardName)
    }

    private fun getZoneNamesInstance(): TimeZoneNames {
        return TimeZoneNames.getInstance(ULocale.getDefault())
    }

    fun getAdapter(context: Activity, zones: List<ZoneInfo>): ArrayAdapter<ZoneInfo> {
        return object : ArrayAdapter<ZoneInfo>(
            context,
            R.layout.time_zone,
            R.id.zone_name,
            zones
        ) {
            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.P)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val desc = view.requireViewById<TextView>(R.id.zone_title)
                val zoneName = view.requireViewById<TextView>(R.id.zone_name)
                if (desc.tag == null) desc.tag = Any()
                zoneName.text = zones[position].displayName
                desc.text = zones[position].standardName.split(" ")[0]
                return view
            }
        }
    }
}