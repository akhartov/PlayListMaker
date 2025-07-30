package com.practicum.playlistmaker.presentation.tracks

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class TrackAdapter(private val trackClickListener: OnTrackClickListener?) :
    RecyclerView.Adapter<TrackViewHolder>(), TrackHistoryInteractor.ChangeListener {

    var tracks = listOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent, trackClickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun clearItems() {
        tracks = listOf()
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<Track>?) {
        if(newItems == null)
            return

        val oldItems = tracks
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackId == newItems[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })

        tracks = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onChange(tracks: List<Track>) {
        updateItems(tracks)
    }

    override fun onClear() {
        clearItems()
    }

}