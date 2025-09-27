package com.practicum.playlistmaker.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment -> {
                    binding.rootControlsGroup.visibility = View.GONE
                }
                else -> {
                    binding.rootControlsGroup.visibility = View.VISIBLE
                }
            }

            binding.backButton.title = when (destination.id) {
                R.id.searchFragment -> resources.getString(R.string.search_text)
                R.id.settingsFragment -> resources.getString(R.string.settings_text)
                else -> resources.getString(R.string.library_text)
            }
        }
    }
}
