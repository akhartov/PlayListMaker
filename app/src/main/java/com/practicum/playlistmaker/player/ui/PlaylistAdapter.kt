package com.practicum.playlistmaker.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.player.ui.PlaylistViewHolder.OnClickListener
import com.practicum.playlistmaker.playlist.domain.PlaylistCover

class PlaylistAdapter(private val clickListener: OnClickListener?): RecyclerView.Adapter<PlaylistViewHolder>() {
    private var items = listOf<PlaylistCover>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.from(parent, clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: List<PlaylistCover>?) {
        if(newItems == null)
            return

        val oldItems = items
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}