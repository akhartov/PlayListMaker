package com.practicum.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    private val storage by lazy { getSharedPreferences("SETTINGS", MODE_PRIVATE) }
    private val NIGHT_MODE_VALUE = "NIGHT_MODE"

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<SwitchMaterial>(R.id.theme_switcher).apply {
            storage.registerOnSharedPreferenceChangeListener { prefs, key ->
                if(key == NIGHT_MODE_VALUE) {
                    (applicationContext as App).switchTheme(prefs.getBoolean(key, AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES))
                }
            }
            setOnCheckedChangeListener { button, isOn ->
                storage.edit().putBoolean(NIGHT_MODE_VALUE, isOn).apply()
            }

            isChecked = storage.getBoolean(NIGHT_MODE_VALUE, applicationContext.resources.configuration.isNightModeActive)
        }


        findViewById<MaterialTextView>(R.id.share_app).setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, resources.getText(R.string.sharing_text))
            }.also { startActivity(it) }
        }

        findViewById<MaterialTextView>(R.id.write_support).setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                setData(Uri.parse("mailto:"))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.email_support)))
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.write_support_subject))
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.write_support_body))
            }.also { startActivity(it) }
        }

        findViewById<MaterialTextView>(R.id.user_agreement).setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                setData(Uri.parse(resources.getString(R.string.uri_link_practicum_offer)))
            }.also { startActivity(it) }
        }
    }
}