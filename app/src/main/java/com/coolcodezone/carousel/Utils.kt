package com.coolcodezone.carousel

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun getScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}