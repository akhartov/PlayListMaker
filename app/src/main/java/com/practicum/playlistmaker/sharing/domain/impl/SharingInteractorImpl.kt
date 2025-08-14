package com.practicum.playlistmaker.sharing.domain.impl

import android.content.res.Resources
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.model.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.dto.EmailData
import com.practicum.playlistmaker.sharing.domain.model.SharingInteractor

class SharingInteractorImpl(
    val resources: Resources,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openAgreement() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun askSupport() {
        externalNavigator.sendEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resources.getString(R.string.sharing_text)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = resources.getString(R.string.email_support),
            subject = resources.getString(R.string.write_support_subject),
            body = resources.getString(R.string.write_support_body)
        )
    }

    private fun getTermsLink(): String {
        return resources.getString(R.string.uri_link_practicum_offer)
    }
}