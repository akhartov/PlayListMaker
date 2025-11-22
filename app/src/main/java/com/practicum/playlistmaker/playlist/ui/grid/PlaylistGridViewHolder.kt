package com.practicum.playlistmaker.playlist.ui.grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistGridViewBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.ui.floatDpToPx

class PlaylistGridViewHolder(
    private val binding: PlaylistGridViewBinding,
    private val clickListener: OnClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cover: PlaylistCover) {
        binding.title.text = cover.title
        binding.info.text = cover.tracksInfo

        Glide.with(itemView.context)
            .load(cover.imagePath)
            .placeholder(R.drawable.track_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    floatDpToPx(
                        itemView.resources,
                        itemView.resources.getDimension(R.dimen.big_image_radius)
                    )
                )
            )
            .into(binding.coverImage)

        itemView.setOnClickListener {
            clickListener?.onPlaylistClick(cover)
        }
    }

    fun interface OnClickListener {
        fun onPlaylistClick(cover: PlaylistCover)
    }

    companion object {
        fun from(parent: ViewGroup, clickListener: OnClickListener?): PlaylistGridViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistGridViewBinding.inflate(inflater, parent, false)
            return PlaylistGridViewHolder(binding, clickListener)
        }
    }
}