package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.practicum.playlistmaker.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditorFragment : BindingFragment<FragmentPlaylistEditorBinding>() {
    private val viewModel: PlaylistEditorViewModel by viewModel {
        parametersOf(requireArguments().getInt(PLAYLIST_ID), requireArguments().getInt(TRACK_ID))
    }
    private var imageUri: Uri? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistEditorBinding {
        return FragmentPlaylistEditorBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            if (imageUri != null && !binding.playlistName.text.isNullOrEmpty() && binding.playlistDescription.text.isNullOrEmpty()) {
                viewModel.savePlaylist(
                    binding.playlistName.text.toString(),
                    binding.playlistDescription.text.toString(),
                    imageUri
                )
            }

            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                imageUri = uri
                binding.coverImage.setImageURI(uri)
            }

        binding.coverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlistCover ->
            binding.playlistName.setText(playlistCover.title)
            binding.playlistDescription.setText(playlistCover.description)
        }
    }

    companion object {
        const val PLAYLIST_ID = "PLAYLIST_ID"
        const val TRACK_ID = "TRACK_ID"

        fun createArgs(playlistId: Int, trackId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId, TRACK_ID to trackId)
    }
}