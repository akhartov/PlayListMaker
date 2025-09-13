package com.practicum.playlistmaker.library.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesTracksFragment: Fragment() {
    private lateinit var binding: FragmentFavouriteTracksBinding
    val tracksViewModel: FavouritesTracksViewModel by viewModel()

            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FavouritesTracksFragment()
    }
}