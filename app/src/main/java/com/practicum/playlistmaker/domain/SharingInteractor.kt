package com.practicum.playlistmaker.domain

interface SharingInteractor {
    fun shareApp()
    fun shareCustomText(text: String)
    fun openAgreement()
    fun askSupport()
}