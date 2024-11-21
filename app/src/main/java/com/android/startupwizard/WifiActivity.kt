package com.android.startupwizard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.startupwizard.adapter.WifiListAdapter

class WifiActivity : AppCompatActivity() {
    private val adapter = WifiListAdapter()
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wifi)
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter
        val list = mutableListOf<Triple<String, String, Int>>()
        list.add(Triple("xiaomi", "none", 1))
        list.add(Triple("xiaom222", "none", 3))
        adapter.setList(list)
    }

}