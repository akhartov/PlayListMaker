package com.practicum.playlistmaker

import android.os.Handler

interface TimerTickListener {
    fun tick(millis: Long)
}

class LooperTimer(
    private val handler: Handler,
    private val listener: TimerTickListener,
    private val extraTime: Long = 0
) {
    private val startTime = System.currentTimeMillis()
    private val DELAY = 1000L
    private var stop = false

    fun start() {
        handler.post(
            createUpdateTimerTask()
        )
    }

    fun pause() {
        stop = true
        val elapsedTime = System.currentTimeMillis() - startTime
        listener.tick(elapsedTime + extraTime)
    }

    fun stop() {
        stop = true
        listener.tick(0)
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                if (stop)
                    handler.removeCallbacks(this)
                else {
                    listener.tick(elapsedTime + extraTime)
                    handler.postDelayed(this, DELAY)
                }
            }
        }
    }
}