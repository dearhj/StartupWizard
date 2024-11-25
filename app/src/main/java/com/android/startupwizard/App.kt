package com.android.startupwizard

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class App: Application() {
    companion object {
        var sp: SharedPreferences? = null
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        sp = PreferenceManager.getDefaultSharedPreferences(this)
    }
}