package com.android.startupwizard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.startupwizard.App.Companion.mSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class FinishWaitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wait)

        Handler(Looper.getMainLooper()).postDelayed({
            handleNavigationOption(mSelection)
            MainScope().launch(Dispatchers.Main) {
                ActivityUtils.finishAllActivity()
            }
            finish()
        }, 2000)
    }
}