package com.practicum.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.api.SettingsInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settings = Creator.getSettingsInteractor()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.themeSwitcher.apply {
            settings.subscribe { key ->
                if (key == SettingsInteractor.NIGHT_MODE_VALUE) {
                    (applicationContext as App).switchTheme(
                        settings.getNightTheme(applicationContext.resources.configuration.isNightModeActive)
                    )
                }
            }
            setOnCheckedChangeListener { _, isOn ->
                settings.setNightTheme(isOn)
            }

            isChecked =
                settings.getNightTheme(applicationContext.resources.configuration.isNightModeActive)
        }

        binding.shareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, resources.getText(R.string.sharing_text))
            }.also { startActivity(it) }
        }

        binding.writeSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                setData(Uri.parse("mailto:"))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.email_support)))
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.write_support_subject))
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.write_support_body))
            }.also { startActivity(it) }
        }

        binding.userAgreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                setData(Uri.parse(resources.getString(R.string.uri_link_practicum_offer)))
            }.also { startActivity(it) }
        }
    }
}