package com.practicum.playlistmaker.player.data.impl

import android.content.Context
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.domain.model.TrackDisplayInteractor
import com.practicum.playlistmaker.player.ui.PlayerFragment

class TrackDisplayInteractorImpl(private val context: Context) : TrackDisplayInteractor {
    override fun show(track: Track) {
        PlayerFragment.newInstance(track)
    }
}