package com.practicum.playlistmaker

import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PlayerActivity : AppCompatActivity() {
    companion object {
        val TRACK = "TRACK"
    }

    private val track by lazy {
        Gson().fromJson<Track>(intent.getStringExtra(TRACK), object : TypeToken<Track>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.back).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<TextView>(R.id.track_title).text = track.trackName
        findViewById<TextView>(R.id.track_artist).text = track.artistName
        findViewById<TextView>(R.id.track_length).text = track.trackTimeMMSS()

        findViewById<Group>(R.id.album_group).isVisible = !track.collectionName.isNullOrEmpty()
        findViewById<TextView>(R.id.track_album).text = track.collectionName

        findViewById<Group>(R.id.year_group).isVisible = track.trackYear().isNotEmpty()
        findViewById<TextView>(R.id.track_year).text = track.trackYear()

        findViewById<Group>(R.id.genre_group).isVisible = !track.primaryGenreName.isNullOrEmpty()
        findViewById<TextView>(R.id.track_genre).text = track.primaryGenreName

        findViewById<TextView>(R.id.track_country).text = track.country

        loadImage()
    }

    private fun loadImage() {
        val coverUrl = track?.getCoverArtwork() ?: return

        Glide.with(applicationContext).load(Uri.parse(coverUrl))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(resources.getDimension(R.dimen.track_big_image_radius))))
            .into(findViewById(R.id.cover))
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            dp,
            resources.displayMetrics
        ).toInt()
    }
}