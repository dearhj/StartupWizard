package com.android.startupwizard

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WaitActivity : AppCompatActivity() {
    private var pingOutTimeFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wait)
        pingOutTimeFlag = false
        ActivityUtils.addActivity(this)
    }

    val handler = Handler(Looper.getMainLooper())
    val runnableBack = Runnable {
        finish()
        toast(this@WaitActivity, this@WaitActivity.getString(R.string.fail_connect_wifi))
    }

    val runnableNext = Runnable {
        startActivityForResult(Intent(this, TimeActivity::class.java), 1)
    }

    val runnablePingOutTime = Runnable { pingOutTimeFlag = true }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) finish()
    }

    override fun onResume() {
        super.onResume()
        registerReceiverWifi()
        updateStatusBar(1)
        handler.postDelayed(runnableBack, 20000)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(wifiReceiver)
        handler.removeCallbacks(runnableBack)
        handler.removeCallbacks(runnableNext)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityUtils.removeActivity(this)
    }

    private fun registerReceiverWifi() {
        val filter = IntentFilter()
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION) //监听wifi连接状态
        registerReceiver(wifiReceiver, filter)
    }

    private val wifiReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NewApi", "NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                if (NetworkInfo.State.DISCONNECTED == info!!.state) {
                } else if (NetworkInfo.State.CONNECTED == info.state) {
                    handler.removeCallbacks(runnableBack)
                    MainScope().launch(Dispatchers.IO) {
                        handler.postDelayed(runnablePingOutTime, 5000)
                        while (!canPing() && !pingOutTimeFlag) {
                            delay(200)
                        }
                        handler.removeCallbacks(runnablePingOutTime)
                        val status = canPing()
                        withContext(Dispatchers.Main) {
                            handler.postDelayed(if (status) runnableNext else runnableBack, 1500)
                        }
                    }
                } else if (NetworkInfo.State.CONNECTING == info.state) {
                }
            }
        }
    }
}