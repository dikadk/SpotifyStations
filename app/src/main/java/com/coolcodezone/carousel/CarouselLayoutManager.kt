package com.coolcodezone.carousel

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class CarouselLayoutManager : LinearLayoutManager {

    private val shrinkAmount = .5f
    private val shrinkDistance = 0.75f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (orientation == VERTICAL) {
            scaleChildren()
            return super.scrollVerticallyBy(dy, recycler, state)
        } else
            return 0
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChildren()
    }

    private fun scaleChildren() {
        val midPoint = height / 2.0f
        val d0 = 0.0f
        val d1 = shrinkDistance * midPoint
        val s0 = 1.0f
        val s1 = 1.0f - shrinkAmount
        var i = 0
        while (i < childCount) {
            val child = getChildAt(i)!!
            val childMidPoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.0f
            val d = min(d1, abs(midPoint - childMidPoint))
            //val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
            Log.e("childMidPoint", childMidPoint.toString())
            Log.e("midPoint", midPoint.toString())
            val scale = if (childMidPoint in midPoint - 50..midPoint + 50) 1.5f else 1.0f
            val transX = if (scale == 1.0f) 0.0f else scale.times(170)
            child.scaleX = scale
            child.scaleY = scale
            child.translationX = transX
            Log.e(TAG, scale.toString())
            i++
        }
    }


    companion object {
        val TAG = CarouselLayoutManager.javaClass.canonicalName
    }
}