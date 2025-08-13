package com.practicum.playlistmaker.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.main.domain.ScreenNavigator

class ScreenNavigatorImpl(private val context: Context): ScreenNavigator {
    override fun showScreen(activityType: Class<out Activity>) {
        Intent(context, activityType).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it)
        }
    }
}