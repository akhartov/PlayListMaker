package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R


class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var isClickHandlingEnabled = true
    private var isTouchInsideBounds = false

    var isPlaying: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                updateBitmap()
                // Перерисовываем View при изменении состояния
                invalidate()
            }
        }

    var currentBitmap: Bitmap? = null
    var playingBitmap: Bitmap? = null
    var stoppedBitmap: Bitmap? = null

    private var imageRect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playingBitmap = getDrawable(R.styleable.PlaybackButtonView_imageResIdPlaying)?.toBitmap()
                stoppedBitmap = getDrawable(R.styleable.PlaybackButtonView_imageResIdStopped)?.toBitmap()
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
                if(isTouchInsideBounds && isClickHandlingEnabled) {
                    return performClick()
                } else
                    return true
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
        currentBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, imageRect, null)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    private fun updateBitmap() {
        currentBitmap = when(isPlaying) {
            true -> playingBitmap
            else -> stoppedBitmap
        }
    }
}