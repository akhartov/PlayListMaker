package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    private fun handleError(ex: Exception) {
        ex.printStackTrace()
        Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<SwitchMaterial>(R.id.switch_night_theme).setOnCheckedChangeListener { button: CompoundButton, isOn: Boolean ->
            //TODO: Toast.makeText(this, if (isOn) "Night" else "Day", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialTextView>(R.id.share_app).setOnClickListener {
            try {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.setType("text/plain")
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT, resources.getText(R.string.sharing_text)
                )

                startActivity(sendIntent)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }

        findViewById<MaterialTextView>(R.id.write_support).setOnClickListener {
            try {
                val shareIntent =
                    Intent(Intent.ACTION_SENDTO, Uri.parse(resources.getString(R.string.uri_email)))
                shareIntent.putExtra(
                    Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.email_support))
                )
                shareIntent.putExtra(
                    Intent.EXTRA_SUBJECT, resources.getString(R.string.write_support_subject)
                )
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT, resources.getString(R.string.write_support_body)
                )
                startActivity(shareIntent)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }

        findViewById<MaterialTextView>(R.id.user_agreement).setOnClickListener {
            try {
                val viewIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(resources.getString(R.string.uri_link_practicum_offer))
                )

                startActivity(viewIntent)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }
}