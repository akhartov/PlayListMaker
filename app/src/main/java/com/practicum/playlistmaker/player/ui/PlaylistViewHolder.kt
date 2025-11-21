package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.ui.floatDpToPx

class PlaylistViewHolder(
    private val binding: PlaylistViewBinding,
    private val clickListener: OnClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cover: PlaylistCover) {
        binding.title.text = cover.title
        binding.info.text = cover.tracksInfo

        Glide.with(itemView.context)
            .load(cover.imagePath)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(floatDpToPx(itemView.resources, itemView.resources.getDimension(R.dimen.image_radius))))
            .into(binding.coverImage)

        itemView.setOnClickListener {
            clickListener?.onPlaylistClick(cover)
        }
    }

    fun interface OnClickListener {
        fun onPlaylistClick(cover: PlaylistCover)
    }

    companion object {
        fun from(parent: ViewGroup, clickListener: OnClickListener?): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistViewBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding, clickListener)
        }
    }
}