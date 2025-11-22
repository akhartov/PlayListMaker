package com.practicum.playlistmaker.root.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistsEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private val rootViewModel: RootViewModel by viewModel()

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.rootControlsGroup.isVisible = when(destination.id) {
                R.id.playerFragment -> false
                R.id.playlistEditorFragment -> false
                R.id.playlistViewerFragment -> false
                else -> true
            }

            binding.backButton.title = when (destination.id) {
                R.id.searchFragment -> resources.getString(R.string.search_text)
                R.id.settingsFragment -> resources.getString(R.string.settings_text)
                else -> resources.getString(R.string.library_text)
            }
        }

        rootViewModel.stateliveData.observe(this) { state ->
            when (state) {
                is PlaylistsEvent.NewPlaylist -> {
                    val message =
                        resources.getString(R.string.new_playlist_created).format(state.title)
                    showCustomToastWithLayout(applicationContext, message)
                }
            }
        }
    }

    fun showCustomToastWithLayout(context: Context, message: String) {
        val toast = Toast(context)

        val layout = layoutInflater.inflate(R.layout.custom_toast, null)
        layout.findViewById<TextView>(R.id.toast_text).text = message
        toast.view = layout

        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.show()
    }
}
