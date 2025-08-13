package com.practicum.playlistmaker.search.ui

import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.domain.models.Track

class TrackViewHolder(private val binding: TrackViewBinding, private val trackClickListener: OnTrackClickListener?)
    : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup, trackClickListener: OnTrackClickListener?): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding, trackClickListener)
        }
    }

    fun bind(track: Track) {
        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.trackTime.text = track.length

        Glide.with(itemView.context)
            .load(Uri.parse(track.artworkUrl100))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(itemView.resources.getDimension(R.dimen.track_image_radius))))
            .into(binding.artworkImage)

        itemView.setOnClickListener {
            trackClickListener?.onTrackClick(track)
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            dp,
            itemView.resources.displayMetrics
        ).toInt()
    }
}