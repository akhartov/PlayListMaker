package com.practicum.playlistmaker.settings.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getFactory())
            .get(SettingsViewModel::class.java)

        binding.backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.GetNightModeLiveData().observe(this) { isNightMode ->
            binding.themeSwitcher.isChecked = isNightMode
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isOn ->
            viewModel.setNightMode(isOn)
        }

        //settings.getNightTheme(applicationContext.resources.configuration.isNightModeActive)


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