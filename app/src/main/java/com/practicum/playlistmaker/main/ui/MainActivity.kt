package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, MainViewModel.getFactory()).get(MainViewModel::class.java)

        binding.buttonSearch.setOnClickListener {
            viewModel.showSearchScreen()
        }

        binding.buttonLibrary.setOnClickListener {
            viewModel.showLibraryScreen()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.showSettingsScreen()
        }
    }
}