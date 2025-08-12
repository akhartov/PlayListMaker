package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.data.model.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.dto.EmailData

class ExternalNavigatorImpl(val context: Context): ExternalNavigator {

    override fun shareLink(link: String) {
        Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { context.startActivity(it) }
    }

    override fun openLink(link: String) {
        Intent(Intent.ACTION_VIEW).apply {
            setData(Uri.parse(link))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { context.startActivity(it) }
    }

    override fun sendEmail(data: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            setData(Uri.parse("mailto:"))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
            putExtra(Intent.EXTRA_SUBJECT, data.subject)
            putExtra(Intent.EXTRA_TEXT, data.body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it)
        }
    }
}