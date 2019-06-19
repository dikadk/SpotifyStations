package com.coolcodezone.carousel

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView


class CustomLayoutManager(val context: Context) : RecyclerView.LayoutManager() {

    private val TAG = "MyLayoutManager"
    private val firstPosition: Int = 0
    private var scrollDistance: Int = 0

    private var decoratedChildWidth: Int = 0
    private var decoratedChildHeight: Int = 0

    init {
        val dm = context.resources.displayMetrics
        scrollDistance = (SCROLL_DISTANCE * dm.density + 0.5f).toInt()
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val parentBottom = height - paddingBottom
        val scrap = if (childCount > 0) getChildAt(0) else null

        var oldTop = paddingTop
        if (scrap != null)
            oldTop = scrap.top

        detachAndScrapAttachedViews(recycler)

        var top = oldTop
        var bottom: Int

        val left = paddingLeft
        val right = width - paddingRight

        var i = 0
        Log.e("ChildCount", itemCount.toString())
        while (i < itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child, i)
            measureChildWithMargins(child, 0, 0)
            bottom = top + getDecoratedMeasuredHeight(child)
            layoutDecoratedWithMargins(child, left, top, right, bottom)

            top = bottom
            i++
        }
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollVertically(): Boolean {
        return itemCount > 1
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        var scrolled = super.scrollVerticallyBy(dy, recycler, state)
        if (childCount == 0)
            return 0

        val recMidPoint = height/2

        return scrolled


    }

    companion object {
        private val SCROLL_DISTANCE = 80 // dp
    }
}