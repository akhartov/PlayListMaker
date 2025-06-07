package com.practicum.playlistmaker


interface OnTracksChangeListener {
    fun onChange(tracks: List<Track>)
    fun onClear()
}