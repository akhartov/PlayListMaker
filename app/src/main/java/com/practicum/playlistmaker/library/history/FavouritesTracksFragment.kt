package com.practicum.playlistmaker.library.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesTracksFragment : BindingFragment<FragmentFavouriteTracksBinding>() {
    private val tracksViewModel: FavouritesTracksViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavouriteTracksBinding {
        return FragmentFavouriteTracksBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FavouritesTracksFragment()
    }
}