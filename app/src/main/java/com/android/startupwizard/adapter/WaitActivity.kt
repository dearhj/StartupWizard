package com.android.startupwizard.adapter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.startupwizard.R


class WaitActivity : AppCompatActivity() {
    private var connectFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wait)
    }

    override fun onResume() {
        super.onResume()
        connectFlag = isWifiConnected()
        registerReceiverWifi()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(wifiReceiver)
    }

    private fun registerReceiverWifi() {
        val filter = IntentFilter()
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION) //监听wifi连接状态
        registerReceiver(wifiReceiver, filter)
    }

    @SuppressLint("MissingPermission")
    private fun isWifiConnected(): Boolean {
        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    private val wifiReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NewApi", "NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            println("监听这里收到了广播变化、、、、   ")
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                if (NetworkInfo.State.DISCONNECTED == info!!.state) {
                    println("监听这里的值时》？   $connectFlag")
                    if (connectFlag) connectFlag = false
                    println("这里i监听到没链接上2222222222222222")//wifi没连接上
                } else if (NetworkInfo.State.CONNECTED == info.state) {
                    println("这里i监听到连接上了11111111111111111111")//wifi连接上了
                } else if (NetworkInfo.State.CONNECTING == info.state) { //正在连接
                    println("这里i监听到正在连接22")
                }
            }
        }
    }
}