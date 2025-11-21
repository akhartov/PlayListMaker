package com.practicum.playlistmaker.ui

import android.content.res.Resources
import android.util.TypedValue

fun floatDpToPx(resources: Resources, dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        dp,
        resources.displayMetrics
    ).toInt()
}