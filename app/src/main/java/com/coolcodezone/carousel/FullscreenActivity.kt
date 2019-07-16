package com.coolcodezone.carousel

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
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
import kotlin.math.min
import kotlin.math.roundToInt
import android.os.Build
import android.util.Log
import android.view.Menu
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper


class FullscreenActivity : AppCompatActivity() {

    private lateinit var stationsRecyclerAdapter: StationsRecyclerAdapter
    private lateinit var linearlayoutManager: RecyclerView.LayoutManager
    private lateinit var snapHelper: LinearSnapHelper

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.plus)
        supportActionBar?.title = "  Add Station"

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        stationsRecyclerAdapter = StationsRecyclerAdapter()

        linearlayoutManager = LinearLayoutManager(this)

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(stationsRecycler)

        stationsRecycler.setPadding(0, getScreenHeight(this) / 2, 0, getScreenHeight(this) / 2)
        stationsRecycler.apply {
            adapter = stationsRecyclerAdapter
            layoutManager = linearlayoutManager
            setHasFixedSize(true)
        }

        var prevSelectedPage = 0
        val snapPosChangeListener = object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                val centerView = snapHelper.findSnapView(linearlayoutManager)
                centerView?.let {
                    if (prevSelectedPage != position)
                        resetOldView(prevSelectedPage)
                    updateSnappedView(position)

                    shakeItBaby()
                    prevSelectedPage = position
                }
            }
        }

        stationsRecycler.attachSnapHelperWithListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            snapPosChangeListener
        )

        stationsRecycler.scrollToPosition(1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    private fun updateSnappedView(position: Int) {
        Log.e("ALALLA", position.toString())
        val currentStation = stationsRecyclerAdapter.stationsList[position]
        val currentStationView = snapHelper.findSnapView(linearlayoutManager)!!

        val stationNameTextView =
            currentStationView.findViewById<TextView>(R.id.stationNameTextView)

        stationNameTextView?.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.activeStation
            )
        )

        val bgDrawable = currentStationView.background ?: stationsRecycler.background
        val startColor = (bgDrawable as ColorDrawable).color
        animateColorChange(startColor, currentStation.color, stationsRecycler)
        animateFontSizeChange(1.2f, 80f, stationNameTextView)
    }

    private fun animateFontSizeChange(
        endSize: Float,
        translateX: Float,
        stationNameTextView: TextView
    ) {
        val animationDuration = 250 // Animation duration in ms

        val animatorScaleX = ObjectAnimator.ofFloat(stationNameTextView, View.SCALE_X, endSize)
        val animatorScaleY = ObjectAnimator.ofFloat(stationNameTextView, View.SCALE_Y, endSize)
        val translateAnimation =
            ObjectAnimator.ofFloat(stationNameTextView, View.TRANSLATION_X, translateX)

        AnimatorSet().apply {
            duration = animationDuration.toLong()
            playTogether(animatorScaleX, animatorScaleY, translateAnimation)
            start()
        }
    }

    private fun animateColorChange(colorFrom: Int, colorTo: Int, view: View) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 250 // milliseconds
        colorAnimation.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
            val colorDrawable = ColorDrawable(it.animatedValue as Int)
            supportActionBar?.setBackgroundDrawable(colorDrawable)
            window.statusBarColor = darkenColor(it.animatedValue as Int)
            playback_background?.background = colorDrawable
            it.startDelay = 500
        }
        colorAnimation.start()
    }

    fun resetOldView(prevSelectedPage: Int) {
        val preStationView = linearlayoutManager.findViewByPosition(prevSelectedPage)
        val stationNameTextView = preStationView?.findViewById<TextView>(R.id.stationNameTextView)
        animateFontSizeChange(1f, 0f, stationNameTextView!!)
        stationNameTextView.apply {
            setTextColor(ContextCompat.getColor(applicationContext, R.color.inactiveStation))
        }
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
        val length: Long = 10
        if (Build.VERSION.SDK_INT >= 26) {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(length)
        }
    }
}

