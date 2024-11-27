package com.android.startupwizard

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.startupwizard.App.Companion.mSelection


class FinishWaitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wait)
        updateStatusBar(1)

        Settings.Global.putInt(contentResolver, Settings.Global.DEVICE_PROVISIONED, 1)
        Settings.Secure.putInt(contentResolver, "user_setup_complete", 1)

        Handler(Looper.getMainLooper()).postDelayed({
            handleNavigationOption(mSelection)
            val pm = packageManager
            val name = ComponentName(this, MainActivity::class.java)
            pm.setComponentEnabledSetting(
                name,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            updateStatusBar(0)
            ActivityUtils.finishAllActivity()
            finish()
        }, 2000)
    }
}