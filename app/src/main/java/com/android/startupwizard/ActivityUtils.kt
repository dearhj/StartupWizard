package com.android.startupwizard

import android.app.Activity

object ActivityUtils {

    private val activityStack: MutableList<Activity> = mutableListOf()

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    fun finishAllActivity() {
        for (i in activityStack.indices) {
            activityStack[i].finish()
        }
        activityStack.clear()
    }
}