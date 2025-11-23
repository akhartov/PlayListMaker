package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistViewerBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.TrackViewHolder
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistViewerFragment : BindingFragment<FragmentPlaylistViewerBinding>() {
    private val viewerViewModel: PlaylistViewerViewModel by viewModel {
        parametersOf(requireArguments().getParcelable(COVER, PlaylistCover::class.java))
    }

    private val trackClickDebounce =
        debounce<Track>(CLICK_TRACK_DEBOUNCE_DELAY, lifecycleScope, true) { track ->
            findNavController().navigate(
                R.id.action_playlistViewerFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

    private var tracksAdapter: TrackAdapter? = TrackAdapter(
        trackClickListener = object : TrackViewHolder.OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                trackClickDebounce(track)
            }

            override fun onTrackLongClick(track: Track): Boolean {
                showDeleteTrackDialog(track)
                return true
            }
        }
    )

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

        initPlaylist()

        initTracks()
    }

    private fun initTracks() {
        binding.tracksRecyclerView.adapter = tracksAdapter
        viewerViewModel.tracksLiveData.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter?.updateItems(tracks)
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.do_you_want_remove_track))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ -> viewerViewModel.deleteTrack(track) }
            .show()
    }

    private fun initPlaylist() {
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
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
        private const val COVER = "COVER"

        fun createArgs(cover: PlaylistCover): Bundle =
            bundleOf(COVER to cover)
    }
}