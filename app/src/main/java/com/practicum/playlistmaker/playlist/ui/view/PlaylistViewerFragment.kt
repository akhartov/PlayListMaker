package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistViewerBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.model.TracksSharingEvent
import com.practicum.playlistmaker.playlist.ui.editor.PlaylistEditorFragment
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
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
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

        binding.tracksRecyclerView.adapter = tracksAdapter
        binding.tracksBottomSheet.isVisible = true

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewerViewModel.coverLiveData.observe(viewLifecycleOwner) { cover ->
            cover?.let {
                initPlaylist(it)
                initMenu(it)
            }
        }

        viewerViewModel.sharingLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is TracksSharingEvent.NoTracksForSharing -> Toast.makeText(requireContext(), resources.getString(R.string.no_tracks_for_sharing), Toast.LENGTH_SHORT).show()
            }
        }

        viewerViewModel.tracksLiveData.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter?.updateItems(tracks)
        }

        binding.playlistShare.setOnClickListener {
            viewerViewModel.sharePlaylist()
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.do_you_want_remove_track))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ -> viewerViewModel.deleteTrack(requireArguments().getInt(PLAYLIST_ID), track) }
            .show()
    }

    private fun showDeleteCoverDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.remove_cover))
            .setMessage(resources.getString(R.string.do_you_want_remove_cover))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                viewerViewModel.deletePlaylist(requireArguments().getInt(PLAYLIST_ID))
                findNavController().navigateUp()
            }
            .show()
    }

    private fun initPlaylist(cover: PlaylistCover) {
            binding.playlistTitle.text = cover.title
            binding.playlistDescription.text = cover.description
            binding.tracksLength.text = cover.tracksMinutesText
            binding.tracksCount.text = cover.tracksCountText
            Glide.with(requireContext())
                .load(cover.imagePath)
                .placeholder(R.drawable.track_placeholder)
                .transform(
                    CenterCrop()
                )
                .into(binding.coverImage)
    }

    private fun initMenu(cover: PlaylistCover) {
        Glide.with(requireContext())
            .load(cover.imagePath)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(floatDpToPx(resources, resources.getDimension(R.dimen.image_radius))))
            .into(binding.coverSmallImage)
        binding.menuPlaylistTitle.text = cover.title
        binding.menuPlaylistInfo.text = cover.tracksCountText

        binding.menuItemSharePlaylist.setOnClickListener {
            viewerViewModel.sharePlaylist()
        }

        binding.menuItemEditPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistViewerFragment_to_playlistEditorFragment,
                PlaylistEditorFragment.createArgs(cover.id)
            )
        }

        binding.menuItemDeletePlaylist.setOnClickListener {
            showDeleteCoverDialog()
        }

        binding.playlistMenu.setOnClickListener {
            binding.menuBottomSheet.isVisible = true
            BottomSheetBehavior.from(binding.menuBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                            binding.tracksBottomSheet.isVisible = true
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                            binding.tracksBottomSheet.isVisible = false
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
        private const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}