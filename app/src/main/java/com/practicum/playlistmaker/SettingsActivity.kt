package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<SwitchMaterial>(R.id.theme_switcher).apply {

            setOnCheckedChangeListener { button, isOn ->
                (applicationContext as App).switchTheme(isOn)
            }
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