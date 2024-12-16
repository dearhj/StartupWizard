package com.android.startupwizard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManagerPolicyConstants.NAV_BAR_MODE_3BUTTON_OVERLAY
import android.view.WindowManagerPolicyConstants.NAV_BAR_MODE_GESTURAL_OVERLAY
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.android.startupwizard.App.Companion.mSelection


class GesturesActivity : AppCompatActivity() {
    private var next: TextView? = null
    private var radioButtonThree: RadioButton? = null
    private var radioButtonGesture: RadioButton? = null
    private var gesture: LottieAnimationView? = null
    private var buttonGesture: LottieAnimationView? = null

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestures)
        radioButtonThree = findViewById(R.id.radio_button)
        radioButtonGesture = findViewById(R.id.radio_gesture)
        gesture = findViewById(R.id.gestural_lottie)
        buttonGesture = findViewById(R.id.button_lottie)

        next = findViewById(R.id.skip)
        next?.text = getString(R.string.set_finish)
        next?.setOnClickListener { startActivity(Intent(this, FinishWaitActivity::class.java)) }
        radioButtonThree?.setOnCheckedChangeListener { _, b ->
            if (b) {
                radioButtonGesture?.isChecked = false
                buttonGesture?.visibility = View.VISIBLE
                gesture?.visibility = View.GONE
                mSelection = NAV_BAR_MODE_3BUTTON_OVERLAY
            } else {
                buttonGesture?.visibility = View.GONE
                gesture?.visibility = View.VISIBLE
                radioButtonGesture?.isChecked = true
                mSelection = NAV_BAR_MODE_GESTURAL_OVERLAY
            }
        }

        radioButtonGesture?.setOnCheckedChangeListener { _, b ->
            if (b) {
                radioButtonThree?.isChecked = false
                buttonGesture?.visibility = View.GONE
                gesture?.visibility = View.VISIBLE
                mSelection = NAV_BAR_MODE_GESTURAL_OVERLAY
            } else {
                buttonGesture?.visibility = View.VISIBLE
                gesture?.visibility = View.GONE
                radioButtonThree?.isChecked = true
                mSelection = NAV_BAR_MODE_3BUTTON_OVERLAY
            }
        }
        ActivityUtils.addActivity(this)
        mSelection = NAV_BAR_MODE_GESTURAL_OVERLAY
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityUtils.removeActivity(this)
    }
}