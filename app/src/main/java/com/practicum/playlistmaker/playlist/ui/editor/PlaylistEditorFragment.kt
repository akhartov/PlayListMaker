package com.practicum.playlistmaker.playlist.ui.editor

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditorFragment : PlaylistCoverFragment() {

    override val viewModel: PlaylistMakerViewModel by viewModel {
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.text = resources.getString(R.string.save)
    }

    companion object {
        const val PLAYLIST_ID = "playlistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}