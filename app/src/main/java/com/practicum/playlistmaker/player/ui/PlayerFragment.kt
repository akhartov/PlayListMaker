package com.practicum.playlistmaker.player.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.ui.presentation.PlayerViewModel
import com.practicum.playlistmaker.player.ui.presentation.PlaylistAdapter
import com.practicum.playlistmaker.player.ui.service.MusicService
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.core.net.toUri


class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    @Suppress("DEPRECATION")
    private fun getTrackFromArgs(): Track? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK, Track::class.java)
        } else {
            requireArguments().getParcelable(TRACK) as Track?
        }
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(getTrackFromArgs())
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            when (service) {
                is MusicService.MusicServiceBinder -> {
                    viewModel.setAudioPlayerControl(service.getService())
                }

                else ->  {
                    service?.let {
                        Log.w(PlayerFragment::class.simpleName, "Unknown binder ${it::class.qualifiedName}")
                    }
                }
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            getTrackFromArgs()?.let { track ->
                putExtra(MusicService.TRACK_URL, track.previewUrl)
                putExtra(MusicService.CONTENT_TITLE, resources.getString(R.string.app_name))
                putExtra(MusicService.CONTENT_MESSAGE, "${track.artistName} - ${track.trackName}")
            }
        }

        requireActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireActivity().unbindService(serviceConnection)
    }

    private val newPlaylistClickDebounce =
        debounce<Any>(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) {
            findNavController().navigate(
                R.id.action_playerFragment_to_playlistMakerFragment
            )
        }

    private val trackToPlaylistClickDebounce =
        debounce<Int>(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) { playlistId ->
            viewModel.addTrackToPlaylist(playlistId)
            BottomSheetBehavior.from(binding.playerBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

    private val playlistsAdapter by lazy {
        PlaylistAdapter { playlist ->
            getTrackFromArgs()?.let { track ->
                trackToPlaylistClickDebounce(playlist.id)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Если выдали разрешение — привязываемся к сервису.
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), resources.getString(R.string.notifications_not_allowed), Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindMusicService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            binding.buttonPlayback.setPlayingSilent(it.isPlaying)
            binding.trackTimePosition.text = it.trackTimePosition
        }

        viewModel.getTrackStateLiveData().observe(viewLifecycleOwner) { state ->
            state.track?.let { showTrackData(it) }
        }

        viewModel.getUserTrackLiveData().observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.buttonChangeFavourites.setImageResource(
                    if (state.isFavourite) R.drawable.ic_button_51_favourite_checked else R.drawable.ic_button_51_favourite_unchecked
                )
            }
        }

        viewModel.playlistsLiveData.observe(viewLifecycleOwner) { playlists ->
            playlistsAdapter.updateItems(playlists)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonPlayback.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
    }

    private fun showTrackData(track: Track) {
        binding.trackTitle.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackLength.text = track.lengthText

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
        Glide.with(requireContext()).load(trackUrl.toUri())
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

    override fun onResume() {
        super.onResume()
        viewModel.onViewResumed()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onViewPaused()
    }

    companion object {
        const val TRACK = "TRACK"
        const val CLICK_DEBOUNCE_DELAY = 500L

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }
}
