package com.android.startupwizard

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.startupwizard.adapter.WifiListAdapter


class WifiActivity : AppCompatActivity() {
    private var adapter: WifiListAdapter? = null
    private var mContext: Context? = null
    private var recyclerView: RecyclerView? = null
    private var mWifiManager: WifiManager? = null
    private var listWifi: MutableList<Triple<String, String, Int>>? = null
    private var progressBar: ProgressBar? = null
    private var driver: View? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mContext = this
        setContentView(R.layout.activity_wifi)
        if (mWifiManager == null)
            mWifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        adapter = WifiListAdapter(mWifiManager)
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter
        val list = mutableListOf<Triple<String, String, Int>>()
        list.add(Triple(getString(R.string.search_wifi), "none", -1))
        adapter?.setList(list)
        registerReceiverWifi()
        findViewById<TextView>(R.id.skip).setOnClickListener {
            startActivity(Intent(this, TimeActivity::class.java))
        }
        findViewById<TextView>(R.id.back).setOnClickListener { finish() }
        progressBar = findViewById(R.id.progress)
        driver = findViewById(R.id.viewDivider)
    }

    override fun onResume() {
        super.onResume()
        registerReceiverWifi()
        progressBar?.visibility = View.VISIBLE
        driver?.visibility = View.GONE
        openWifi(mWifiManager)
        startScanWifi(mWifiManager)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(wifiReceiver)
    }

    private fun registerReceiverWifi() {
        val filter = IntentFilter()
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) //监听wifi列表变化（开启一个热点或者关闭一个热点）
        registerReceiver(wifiReceiver, filter)
    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        if (mWifiManager == null) super.onBackPressed()
//    }


    private val wifiReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NewApi", "NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == intent.action) {
                try {
                    progressBar?.visibility = View.GONE
                    driver?.visibility = View.VISIBLE
                    listWifi = mutableListOf()
                    listWifi?.clear()
                    var type: String
                    var name = ""
                    mWifiManager?.let {
                        val list = getWifiList(it).sortedByDescending { wifiInfo -> wifiInfo.level }
                        if (list.isNotEmpty()) {
                            val seen = mutableSetOf<String>()
                            list.forEach { wifi ->
                                val capabilities: String = wifi.capabilities
                                type = if (!TextUtils.isEmpty(capabilities)) {
                                    if (capabilities.contains("WPA") || capabilities.contains("wpa")) "WPA"
                                    else if (capabilities.contains("WEP") || capabilities.contains("wep")) "WEP"
                                    else "NO_PASSWORD"
                                } else "UN_KNOW"
                                if (wifi.wifiSsid.toString()
                                        .startsWith("\"") && wifi.wifiSsid.toString().endsWith("\"")
                                ) name = wifi.wifiSsid.toString()
                                    .substring(1, wifi.wifiSsid.toString().length - 1)
                                val item = Triple(
                                    name, type, WifiManager.calculateSignalLevel(wifi.level, 10)
                                )
                                if (seen.add(item.first) && item.first != "") listWifi?.add(item)
                            }
                        } else listWifi?.add(
                            Triple(
                                mContext!!.getString(R.string.no_wifi),
                                "none",
                                -1
                            )
                        )
                    }
                    adapter?.setList(listWifi)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}