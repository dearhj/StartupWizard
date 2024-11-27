package com.android.startupwizard

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object ActivityUtils {
    private val scope = MainScope()

    private val activityStack: MutableList<Activity> = mutableListOf()

    fun addActivity(activity: Activity) {
        scope.launch(Dispatchers.Main) {
            activityStack.add(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        scope.launch(Dispatchers.Main) {
            activityStack.remove(activity)
        }
    }

    fun finishAllActivity() {
        scope.launch(Dispatchers.Main) {
            for (i in activityStack.indices) {
                activityStack[i].finish()
            }
            activityStack.clear()
        }
    }
}