package com.practicum.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistGridViewBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.ui.floatDpToPx

class PlaylistGridViewHolder(
    private val binding: PlaylistGridViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cover: PlaylistCover) {
        binding.title.text = cover.title
        binding.info.text = cover.tracksInfo


        Glide.with(itemView.context)
            .load(cover.imagePath)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(floatDpToPx(itemView.resources, itemView.resources.getDimension(R.dimen.big_image_radius))))
            .into(binding.coverImage)
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistGridViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistGridViewBinding.inflate(inflater, parent, false)
            return PlaylistGridViewHolder(binding)
        }
    }
}