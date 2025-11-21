package com.practicum.playlistmaker.player.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(requireArguments().getParcelable(TRACK, Track::class.java))
    }

    private val newPlaylistClickDebounce =
        debounce<Any>(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) {
            findNavController().navigate(
                R.id.action_playerFragment_to_playlistEditorFragment
            )
        }

    private val trackToPlaylistClickDebounce =
        debounce<Int>(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) { playlistId ->
            viewModel.trackToPlaylist(playlistId)
            BottomSheetBehavior.from(binding.playerBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

    private val playlistsAdapter by lazy {
        PlaylistAdapter { playlist ->
            requireArguments().getParcelable(TRACK, Track::class.java)?.let { track ->
                trackToPlaylistClickDebounce(playlist.id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStateLiveData().observe(viewLifecycleOwner) { state ->
            state.track?.let { showTrackData(it) }
            state.isPlaying?.let { isPlaying -> binding.buttonPlay.setImageResource(if (isPlaying == true) R.drawable.ic_button_pause_100 else R.drawable.ic_button_play_100) }
            state.trackTimePosition?.let { positionText ->
                binding.trackTimePosition.text = positionText
            }
        }

        viewModel.getUserTrackLiveData().observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.buttonChangeFavourites.setImageResource(
                    if (state.isFavourite) R.drawable.ic_button_51_favourite_checked else R.drawable.ic_button_51_favourite_unchecked
                )
            }
        }

        viewModel.playlistLiveData.observe(viewLifecycleOwner) { items ->
            playlistsAdapter.updateItems(items)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.clickPlay()
        }

        binding.buttonAddToList.setOnClickListener {
            BottomSheetBehavior.from(binding.playerBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.buttonChangeFavourites.setOnClickListener {
            viewModel.tapFavouriteTrack()
        }

        binding.newPlaylistButton.setOnClickListener {
            newPlaylistClickDebounce(0)
        }

        binding.playlistsRecyclerView.adapter = playlistsAdapter


        BottomSheetBehavior.from(binding.playerBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun showTrackData(track: Track) {
        binding.trackTitle.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackLength.text = track.length

        binding.albumGroup.isVisible = track.collectionName.isNotEmpty()
        binding.trackAlbum.text = track.collectionName

        binding.yearGroup.isVisible = track.trackYear.isNotEmpty()
        binding.trackYear.text = track.trackYear

        binding.genreGroup.isVisible = track.primaryGenreName.isNotEmpty()
        binding.trackGenre.text = track.primaryGenreName

        binding.trackCountry.text = track.country

        loadImage(track.coverArtwork)
    }

    private fun loadImage(trackUrl: String) {
        Glide.with(requireContext()).load(Uri.parse(trackUrl))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    floatDpToPx(
                        resources,
                        resources.getDimension(R.dimen.big_image_radius)
                    )
                )
            )
            .into(binding.cover)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        const val TRACK = "TRACK"
        const val CLICK_DEBOUNCE_DELAY = 500L

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }
}