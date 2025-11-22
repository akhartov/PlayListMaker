package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistViewerBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistViewerFragment: BindingFragment<FragmentPlaylistViewerBinding>() {
    private val viewerViewModel: PlaylistViewerViewModel by viewModel {
        parametersOf(requireArguments().getParcelable(COVER, PlaylistCover::class.java))
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistViewerBinding {
        return FragmentPlaylistViewerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewerViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            binding.playlistTitle.text = state.title
            binding.playlistDescription.text = state.description
            binding.tracksLength.text = state.tracksLength
            binding.tracksCount.text = state.tracksCount
            Glide.with(requireContext())
                .load(state.imagePath)
                .placeholder(R.drawable.track_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        floatDpToPx(
                            resources,
                            resources.getDimension(R.dimen.big_image_radius)
                        )
                    )
                )
                .into(binding.coverImage)
        }
    }

    companion object {
        const val COVER = "COVER"

        fun createArgs(cover: PlaylistCover): Bundle =
            bundleOf(COVER to cover)
    }
}