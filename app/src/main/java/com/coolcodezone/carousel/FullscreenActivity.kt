package com.coolcodezone.carousel

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_fullscreen.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlin.math.min
import kotlin.math.roundToInt
import android.os.Build
import androidx.recyclerview.widget.SnapHelper


class FullscreenActivity : AppCompatActivity(){

    private lateinit var stationsRecyclerAdapter: StationsRecyclerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var snapHelper: SnapHelper

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        stationsRecyclerAdapter = StationsRecyclerAdapter()
        viewManager = CarouselLayoutManager(this)

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(stationsRecycler)

        stationsRecycler.apply {
            adapter = stationsRecyclerAdapter
            layoutManager = viewManager
        }

        var prevSelectedPage: Int = 0
        val snapPosChangeListener = object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                if (position != prevSelectedPage) {
                    resetOldView(prevSelectedPage)
                }

                updateSnappedView(position)

                shakeItBaby()
                prevSelectedPage = position
            }

        }

        stationsRecycler.attachSnapHelperWithListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            snapPosChangeListener
        )
    }

    private fun updateSnappedView(position: Int) {
        val currentStation = stationsRecyclerAdapter.stationsList[position]
        val currentStationView = snapHelper.findSnapView(viewManager)!!
        currentStationView.findViewById<TextView>(R.id.stationNameTextView)
            .setTextColor(ContextCompat.getColor(applicationContext, R.color.activeStation))
        val bgDrawable = currentStationView.background ?: stationsRecycler.background
        val startColor = (bgDrawable as ColorDrawable).color
        animateColorChange(startColor, currentStation.color, stationsRecycler)
    }

    fun animateColorChange(colorFrom: Int, colorTo: Int, view: View) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 250 // milliseconds
        colorAnimation.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it.animatedValue as Int))
            window.statusBarColor = darkenColor(it.animatedValue as Int)
            it.startDelay = 500
        }
        colorAnimation.start()
    }

    fun resetOldView(prevSelectedPage: Int) {
        val preStationView = viewManager.findViewByPosition(prevSelectedPage)
        preStationView?.findViewById<TextView>(R.id.stationNameTextView)
            ?.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.inactiveStation
                )
            )
    }

    private fun darkenColor(color: Int): Int {
        val factor = 0.8f
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.argb(
            a,
            min(r, 255),
            min(g, 255),
            min(b, 255)
        )
    }

    private fun shakeItBaby() {
        val length:Long = 30
        if (Build.VERSION.SDK_INT >= 26) {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(length, 10))
        } else {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(length)
        }
    }
}

