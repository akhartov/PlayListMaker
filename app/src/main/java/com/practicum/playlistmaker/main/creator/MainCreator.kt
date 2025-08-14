package com.practicum.playlistmaker.main.creator

import android.app.Application
import com.practicum.playlistmaker.main.data.ScreenNavigatorImpl
import com.practicum.playlistmaker.main.domain.ScreenNavigator

object MainCreator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    fun getScreenNevigator(): ScreenNavigator {
        return ScreenNavigatorImpl(application.applicationContext)
    }
}