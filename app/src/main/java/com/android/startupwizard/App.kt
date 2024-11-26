package com.android.startupwizard

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.WindowManagerPolicyConstants.NAV_BAR_MODE_GESTURAL_OVERLAY

class App: Application() {
    companion object {
        var sp: SharedPreferences? = null
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
        var mSelection = NAV_BAR_MODE_GESTURAL_OVERLAY
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        mSelection = NAV_BAR_MODE_GESTURAL_OVERLAY
        handleNavigationOption(mSelection) //设置为手势导航
    }
}