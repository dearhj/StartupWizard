package com.android.startupwizard.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.wifi.WifiManager
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.startupwizard.R
import com.android.startupwizard.connectWifi
import com.android.startupwizard.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class WifiListAdapter(private val mWifiManager: WifiManager?) :
    BaseQuickAdapter<Triple<String, String, Int>, BaseViewHolder>(R.layout.wifi_recycleview) {
    override fun convert(holder: BaseViewHolder, item: Triple<String, String, Int>) {
        val image = holder.getView<ImageView>(R.id.image)
        val wifiName = holder.getView<TextView>(R.id.name)
        val wifiInfo = holder.getView<LinearLayout>(R.id.wifiInfo)
        wifiName.setTextColor(Color.BLACK)
        if (item.third == -1) {
            image.setImageResource(R.drawable.ic_wifi_scan)
            wifiName.setTextColor(Color.GRAY)
        } else if (item.third >= 9) {
            if (item.second != "NO_PASSWORD") image.setImageResource(R.drawable.ic_wifi_signal_lock_4)
            else image.setImageResource(R.drawable.ic_wifi_signal_4)
        } else if (item.third in 7..8) {
            if (item.second != "NO_PASSWORD") image.setImageResource(R.drawable.ic_wifi_signal_lock_3)
            else image.setImageResource(R.drawable.ic_wifi_signal_3)
        } else if (item.third in 5..6) {
            if (item.second != "NO_PASSWORD") image.setImageResource(R.drawable.ic_wifi_signal_lock_2)
            else image.setImageResource(R.drawable.ic_wifi_signal_2)
        } else if (item.third in 3..4) {
            if (item.second != "NO_PASSWORD") image.setImageResource(R.drawable.ic_wifi_signal_lock_1)
            else image.setImageResource(R.drawable.ic_wifi_signal_1)
        } else if (item.third in 0..2) {
            if (item.second != "NO_PASSWORD") image.setImageResource(R.drawable.ic_wifi_signal_lock_0)
            else image.setImageResource(R.drawable.ic_wifi_signal_0)
        }
        wifiName.text = item.first
        wifiInfo.setOnClickListener {
            if (item.second != "NO_PASSWORD") showPasswordDialog(context, item.first) {
                val password = it.findViewById<EditText>(R.id.password).text.toString()
                connectWifi(mWifiManager, item.first, password, item.second)
                context.startActivity(Intent(context, WaitActivity::class.java))
            } else {
                connectWifi(mWifiManager, item.first, "", item.second)
                context.startActivity(Intent(context, WaitActivity::class.java))
            }
        }
    }

    private fun showPasswordDialog(
        context: Context,
        title: String,
        ok: ((View) -> Unit)
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle(title)
        val v = View.inflate(context, R.layout.dialog_password, null)
        val et = v.findViewById<EditText>(R.id.password)
        val cb = v.findViewById<CheckBox>(R.id.cb_show_password)
        cb.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) et.inputType = 1 else et.inputType = 129
            et.setSelection(et.text.length)
        }
        alertBuilder.setView(v)
        alertBuilder.setCancelable(false)
        alertBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
            if (et.text.isEmpty()) toast(context, "密码为空")
            else ok(v)
        }
        alertBuilder.setNegativeButton(android.R.string.cancel, null)
        alertBuilder.create().show()
        et.requestFocus()
    }


}