package com.practicum.playlistmaker

import android.os.Handler

interface TimerTickListener {
    fun onTickTimer()
    fun onResetTimer()
}

class LooperTimer(
    private val handler: Handler,
    private val listener: TimerTickListener
) {
    private var stop = true

    companion object {
        private const val DELAY = 1000L
    }

    fun start() {
        if(stop) {
            stop = false
            handler.post(
                createUpdateTimerTask()
            )
        }
    }

    fun pause() {
        if (!stop) {
            stop = true
            listener.onTickTimer()
        }
    }

    fun stop() {
        stop = true
        listener.onResetTimer()
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (stop)
                    handler.removeCallbacks(this)
                else {
                    listener.onTickTimer()
                    handler.postDelayed(this, DELAY)
                }
            }
        }
    }
}