package com.android.startupwizard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.internal.app.LocalePicker
import com.android.internal.app.LocalePicker.LocaleInfo
import com.android.internal.app.LocalePicker.updateLocale
import com.android.startupwizard.App.Companion.sp

class MainActivity : AppCompatActivity() {
    private var language: LinearLayout? = null
    private var startButton: Button? = null
    private var chooseLanguage: TextView? = null
    private var currentLanguage = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        language = findViewById(R.id.language)
        chooseLanguage = findViewById(R.id.choose_language)
        startButton = findViewById(R.id.start)
        language?.setOnClickListener { showLanguagePicker(this) }
        startButton?.setOnClickListener {
            startActivity(Intent(this, WifiActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        currentLanguage = sp?.getString("language", "none") ?: "none"
        chooseLanguage?.text = getDisplayName(currentLanguage)
    }


    private fun constructLocaleAdapter(activity: Activity): ArrayAdapter<LocaleInfo> {
        val adapter = LocalePicker.constructAdapter(activity)
        var localeInfo: LocaleInfo? = null
        for (index in 0..<adapter.count) {
            val item = adapter.getItem(index)
            if (item?.locale?.toLanguageTag() == currentLanguage) {
                localeInfo = item
                adapter.remove(localeInfo)
                break
            }
        }
        if (localeInfo != null) adapter.insert(localeInfo, 0)
        return adapter
    }

    private fun showLanguagePicker(activity: Activity) {
        val adapter = constructLocaleAdapter(activity)
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.chooseLanguageTitle))
            .setAdapter(adapter) { _, which ->
                updateLocale(adapter.getItem(which)!!.locale)
                currentLanguage = LocalePicker.getLocales()[0].toLanguageTag()
                chooseLanguage?.text = getDisplayName(currentLanguage)
                sp?.edit()?.putString("language", currentLanguage)?.apply()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }
}