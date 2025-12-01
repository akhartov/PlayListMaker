package com.practicum.playlistmaker.mediacollection.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.favourites.ui.FavouritesTracksFragment
import com.practicum.playlistmaker.playlist.ui.grid.PlaylistsFragment

class MediaCollectionViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouritesTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}