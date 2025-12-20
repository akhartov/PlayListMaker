package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.practicum.playlistmaker.R


class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var isClickHandlingEnabled = true
    private var isTouchInsideBounds = false

    private var currentDraw: Drawable? = null
    private var playingDraw: Drawable? = null
    private var stoppedDraw: Drawable? = null

    var isPlaying: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                updateBitmap()
                invalidate()
            }
        }


    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PlaybackButtonView, defStyleAttr, defStyleRes
        ).apply {
            try {
                playingDraw = getDrawable(R.styleable.PlaybackButtonView_imageResIdPlaying)
                stoppedDraw = getDrawable(R.styleable.PlaybackButtonView_imageResIdStopped)
            } finally {
                recycle()
            }
        }

        updateBitmap()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouchInsideBounds = true
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                isTouchInsideBounds = (x >= 0 && x < width && y >= 0 && y < height)
            }

            MotionEvent.ACTION_UP -> {
                if (isTouchInsideBounds && isClickHandlingEnabled) {
                    return performClick()
                } else return true
            }

            else -> {
                isTouchInsideBounds = false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        isPlaying = !isPlaying
        return super.performClick()
    }

    fun setPlayingSilent(value: Boolean) {
        isClickHandlingEnabled = false
        isPlaying = value
        isClickHandlingEnabled = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        currentDraw?.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        playingDraw?.setBounds(0, 0, w, h)
        stoppedDraw?.setBounds(0, 0, w, h)

    }

    private fun updateBitmap() {
        currentDraw = if (isPlaying) playingDraw else stoppedDraw
    }
}
