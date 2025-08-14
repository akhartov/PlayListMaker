package com.practicum.playlistmaker.sharing.data.model

import com.practicum.playlistmaker.sharing.data.dto.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun sendEmail(data: EmailData)
}