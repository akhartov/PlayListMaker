package com.practicum.playlistmaker.library.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistGridViewBinding
import com.practicum.playlistmaker.playlist.domain.PlaylistCover

class PlaylistGridViewHolder(
    private val binding: PlaylistGridViewBinding,
    private val clickListener: OnClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun interface OnClickListener {
        fun onPlaylistClick(cover: PlaylistCover)
    }

    fun bind(cover: PlaylistCover) {
        binding.title.text = cover.title
        binding.info.text = cover.tracksInfo


        Glide.with(itemView.context)
            .load(cover.imagePath)
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(itemView.resources.getDimension(R.dimen.track_image_radius))))
            .into(binding.coverImage)

        itemView.setOnClickListener {
            clickListener?.onPlaylistClick(cover)
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            dp,
            itemView.resources.displayMetrics
        ).toInt()
    }

    companion object {
        fun from(parent: ViewGroup, clickListener: OnClickListener?): PlaylistGridViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistGridViewBinding.inflate(inflater, parent, false)
            return PlaylistGridViewHolder(binding, clickListener)
        }
    }
}