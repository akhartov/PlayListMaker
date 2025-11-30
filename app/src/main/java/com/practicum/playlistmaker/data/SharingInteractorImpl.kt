package com.practicum.playlistmaker.data

import android.content.res.Resources
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.ExternalNavigator
import com.practicum.playlistmaker.data.dto.EmailData
import com.practicum.playlistmaker.domain.SharingInteractor

class SharingInteractorImpl(
    val resources: Resources,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareText(getShareAppLink())
    }

    override fun shareCustomText(text: String) {
        externalNavigator.shareText(text)
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