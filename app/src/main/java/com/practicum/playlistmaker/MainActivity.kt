package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //setContentView(R.layout.activity_settings)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/


        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonLibrary = findViewById<Button>(R.id.button_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSearch.setOnClickListener {
            Toast.makeText(this, "Нажал ${buttonSearch.text}", Toast.LENGTH_SHORT).show()
        }
        buttonLibrary.setOnClickListener {
            Toast.makeText(this, "Нажал ${buttonLibrary.text}", Toast.LENGTH_SHORT).show()
        }
        buttonSettings.setOnClickListener {
            Toast.makeText(this, "Нажал ${buttonSettings.text}", Toast.LENGTH_SHORT).show()
        }
    }
}