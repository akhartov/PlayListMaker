package com.practicum.playlistmaker.main.domain

import android.app.Activity

interface ScreenNavigator {
    fun showScreen(activityType: Class<out Activity>)
}