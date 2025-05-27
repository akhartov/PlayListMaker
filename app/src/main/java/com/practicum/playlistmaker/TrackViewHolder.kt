package com.practicum.playlistmaker

import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
) {
    private val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    private val artworkImage = itemView.findViewById<ImageView>(R.id.artwork_image)

    fun bind(track: Track) {
        artistName.text = track.artistName
        trackName.text = track.trackName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(itemView.context)
            .load(Uri.parse(track.artworkUrl100))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(itemView.resources.getDimension(R.dimen.track_image_radius))))
            .into(artworkImage)
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            dp,
            itemView.resources.displayMetrics
        ).toInt()
    }
}