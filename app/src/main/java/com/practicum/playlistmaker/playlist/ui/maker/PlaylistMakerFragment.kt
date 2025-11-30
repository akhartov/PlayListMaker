package com.practicum.playlistmaker.playlist.ui.maker

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.PlaylistCoverFragment
import com.practicum.playlistmaker.playlist.ui.PlaylistCoverViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMakerFragment : PlaylistCoverFragment() {
    val makerViewModel: PlaylistMakerViewModel by viewModel()

    override fun getViewModel(): PlaylistCoverViewModel = makerViewModel

    private lateinit var backCallback: OnBackPressedCallback
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.text = resources.getString(R.string.create)
        binding.newPlaylistButton.setOnClickListener {
            if (!binding.playlistTitle.text.isNullOrEmpty()) {
                makerViewModel.makePlaylistCover()
            }

            findNavController().navigateUp()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.ask_finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_data_will_lost))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
            }.setNegativeButton(resources.getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPreviousView()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backCallback)

        binding.backButton.setOnClickListener {
            backPreviousView()
        }
    }

    private fun backPreviousView() {
        if (makerViewModel.hasUserTypedText())
            confirmDialog.show()
        else
            findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backCallback.remove()
    }
}

