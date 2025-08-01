package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.library.LibraryActivity
import com.practicum.playlistmaker.presentation.search.SearchActivity
import com.practicum.playlistmaker.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_search).setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        findViewById<Button>(R.id.button_library).setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        findViewById<Button>(R.id.button_settings).setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}