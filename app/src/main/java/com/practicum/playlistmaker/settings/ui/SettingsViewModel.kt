package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.model.NightModeInteractor
import com.practicum.playlistmaker.sharing.domain.model.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val nightModeInteractor: NightModeInteractor
) : ViewModel() {

    private val nightModeLiveData = MutableLiveData(nightModeInteractor.getNightMode())
    fun getNightModeLiveData(): LiveData<Boolean> = nightModeLiveData

    fun setNightMode(isNight: Boolean) {
        nightModeInteractor.setNightMode(isNight)
        nightModeLiveData.postValue(isNight)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun askSupport() {
        sharingInteractor.askSupport()
    }

    fun openAgreement() {
        sharingInteractor.openAgreement()
    }
}