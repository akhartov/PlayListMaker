package com.practicum.playlistmaker.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker.favourites.domain.FavouritesState
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesTracksFragment : BindingFragment<FragmentFavouriteTracksBinding>() {
    private var tracksAdapter: TrackAdapter? = TrackAdapter { track ->
        tracksClickDebounce(track)
    }

    private val tracksClickDebounce =
        debounce<Track>(CLICK_TRACK_DEBOUNCE_DELAY, lifecycleScope, true) { track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

    private val viewModel: FavouritesTracksViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavouriteTracksBinding {
        return FragmentFavouriteTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start()

        initViewModel()
    }

    private fun initViewModel() {
        binding.recyclerView.adapter = tracksAdapter
        viewModel.apply {
            getStateLiveData().observe(viewLifecycleOwner) { state ->
                when (state) {
                    is FavouritesState.Loading -> {
                        binding.noTracksGroup.isVisible = false
                        tracksAdapter?.updateItems(emptyList())
                        binding.recyclerView.isVisible = false
                    }
                    is FavouritesState.Empty -> {
                        binding.noTracksGroup.isVisible = true
                        tracksAdapter?.updateItems(emptyList())
                        binding.recyclerView.isVisible = false
                    }
                    is FavouritesState.Content -> {
                        binding.noTracksGroup.isVisible = false
                        tracksAdapter?.updateItems(state.tracks)
                        binding.recyclerView.isVisible = true
                    }
                }
            }


        }
    }

    companion object {
        fun newInstance() = FavouritesTracksFragment()

        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}