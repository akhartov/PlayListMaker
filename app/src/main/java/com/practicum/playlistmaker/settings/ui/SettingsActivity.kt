package com.practicum.playlistmaker.settings.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.getNightModeLiveData().observe(this) { isNightMode ->
            binding.themeSwitcher.isChecked = isNightMode
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isOn ->
            viewModel.setNightMode(isOn)
        }

        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeSupport.setOnClickListener {
            viewModel.askSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openAgreement()
        }
    }
}