package com.android.startupwizard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView


class GesturesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestures)
        findViewById<TextView>(R.id.skip).setOnClickListener {
            findViewById<LottieAnimationView>(R.id.button_lottie).visibility = View.VISIBLE
            findViewById<LottieAnimationView>(R.id.gestural_lottie).visibility = View.GONE

        }
    }
}