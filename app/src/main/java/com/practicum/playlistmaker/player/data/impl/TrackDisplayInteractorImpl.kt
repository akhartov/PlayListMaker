package com.practicum.playlistmaker.player.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.domain.model.TrackDisplayInteractor
import com.practicum.playlistmaker.player.ui.PlayerActivity

class TrackDisplayInteractorImpl(private val context: Context) : TrackDisplayInteractor {
    override fun show(track: Track) {
        Intent(context, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.TRACK, track)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            context.startActivity(it)
        }
    }
}