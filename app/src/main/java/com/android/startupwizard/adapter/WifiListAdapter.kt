package com.android.startupwizard.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.startupwizard.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class WifiListAdapter :
    BaseQuickAdapter<Triple<String, String, Int>, BaseViewHolder>(R.layout.wifi_recycleview) {
    override fun convert(holder: BaseViewHolder, item: Triple<String, String, Int>) {
        val image = holder.getView<ImageView>(R.id.image)
        val wifiName = holder.getView<TextView>(R.id.name)
        val wifiInfo = holder.getView<LinearLayout>(R.id.wifiInfo)
        if (item.third > 2) image.setImageResource(R.drawable.ic_wifi_signal_4)
        else image.setImageResource(R.drawable.ic_wifi_signal_2)
        wifiName.text = item.first
        wifiInfo.setOnClickListener {
            println("点击了WiFi详情")
        }
    }
}