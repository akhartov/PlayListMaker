package com.practicum.playlistmaker.sharing.creator

import android.app.Application
import com.practicum.playlistmaker.sharing.data.model.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.model.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object SharingCreator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        SharingCreator.application = application
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application.applicationContext)
    }

    fun getSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(application.applicationContext.resources, getExternalNavigator())
    }
}