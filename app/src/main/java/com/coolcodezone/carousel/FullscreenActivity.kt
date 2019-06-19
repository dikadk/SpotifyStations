package com.coolcodezone.carousel

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_fullscreen.*
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.drawable.ColorDrawable


class FullscreenActivity : AppCompatActivity(), OnSnapPositionChangeListener {
    override fun onSnapPositionChange(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var stationsRecyclerAdapter: StationsRecyclerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        stationsRecyclerAdapter = StationsRecyclerAdapter()
        viewManager = CarouselLayoutManager(this)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(stationsRecycler)

        stationsRecycler.apply {
            adapter = stationsRecyclerAdapter
            layoutManager = viewManager
        }

        val snapPosChangeListener = object : OnSnapPositionChangeListener {
            override fun onSnapPositionChange(position: Int) {
                val currentStation = stationsRecyclerAdapter.stationsList[position]
                val currentStationView = snapHelper.findSnapView(viewManager)!!
                val bgDrawable = currentStationView.background ?: stationsRecycler.background
                val startColor = (bgDrawable as ColorDrawable).color
                animateColorChange(startColor, currentStation.color, stationsRecycler)

            }

        }

        stationsRecycler.attachSnapHelperWithListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            snapPosChangeListener
        )
/*
        var prevView: View? = null
        stationsRecycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val stationNameTextView = snapHelper.findSnapView(viewManager)
                ?.findViewById<TextView>(R.id.stationNameTextView)
            stationNameTextView?.isSelected = true
            stationNameTextView?.textSize = 56.0f
            stationNameTextView?.movementMethod = ScrollingMovementMethod()

            if (prevView != null) {
                val prevStationName = prevView!!.findViewById<TextView>(R.id.stationNameTextView)
                prevStationName.isSelected = false
                prevStationName.textSize = 48.0f
                prevStationName.movementMethod = null
            }

            prevView = snapHelper.findSnapView(viewManager)
        }*/

    }

    fun animateColorChange(colorFrom: Int, colorTo: Int, view: View) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 250 // milliseconds
        colorAnimation.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it.animatedValue as Int))
            it.startDelay = 300
        }
        colorAnimation.start()
    }
}

