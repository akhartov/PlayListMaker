package com.practicum.playlistmaker.library.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: BindingFragment<FragmentPlaylistsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            playlistsViewModel.createNewPlaylist()
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}