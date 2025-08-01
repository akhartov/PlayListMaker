package com.practicum.playlistmaker.domain.impl

import android.util.Log
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.managers.SettingsManager

class SettingsInteractorImpl(private val settingsManager: SettingsManager) : SettingsInteractor {
    override fun setNightTheme(isNight: Boolean) {
        settingsManager.setString(SettingsInteractor.NIGHT_MODE_VALUE, isNight.toString())
    }

    override fun getNightTheme(defaultValue: Boolean): Boolean {
        return try {
            settingsManager.getString(
                SettingsInteractor.NIGHT_MODE_VALUE,
                defaultValue.toString()
            ).toBoolean()
        } catch (e: ClassCastException) {
            Log.e(javaClass.name, e.message ?: e.toString())
            settingsManager.cleanup()
            false
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message ?: e.toString())
            false
        }
    }

    override fun subscribe(listener: (String) -> Unit) {
        settingsManager.subscribe(listener)
    }
}