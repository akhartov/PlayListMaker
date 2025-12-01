package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.data.dto.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(link: String)
    fun sendEmail(data: EmailData)
}