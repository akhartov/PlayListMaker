package com.practicum.playlistmaker.playlist.ui.editor

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditorFragment : PlaylistCoverFragment() {

    override val viewModel: PlaylistCoverViewModel by viewModel {
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.text = resources.getString(R.string.save)

        viewModel.coverLiveData.observe(viewLifecycleOwner) { cover ->
            binding.playlistTitle.setText(cover.title)
            binding.playlistDescription.setText(cover.description)

            Glide.with(requireContext())
                    .load(cover.imagePath)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            floatDpToPx(resources, resources.getDimension(R.dimen.big_image_radius))
                        )
                    )
                    .into(binding.coverImage)
        }

        binding.newPlaylistButton.setOnClickListener {
            if (!binding.playlistTitle.text.isNullOrEmpty()) {
                viewModel.savePlaylist()
            }

            findNavController().navigateUp()
        }
    }

    companion object {
        const val PLAYLIST_ID = "playlistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}