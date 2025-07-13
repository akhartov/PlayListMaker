package com.practicum.playlistmaker

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PlayerActivity : AppCompatActivity(), TimerTickListener {
    companion object {
        val TRACK = "TRACK"
    }

    val trackPlayPauseButton by lazy { findViewById<ImageButton>(R.id.button_play) }
    val timerTrackPosition by lazy { findViewById<TextView>(R.id.track_time_position) }

    private val track by lazy {
        Gson().fromJson<Track>(intent.getStringExtra(TRACK), object : TypeToken<Track>() {}.type)
    }

    private val player by lazy {
        val timer = LooperTimer(mainThreadHandler, this@PlayerActivity)

        val mediaPlayerListener = object : MediaPlayerListener {
            override fun onReadyToPlay() {
                trackPlayPauseButton.isEnabled = true
                trackPlayPauseButton.setImageResource(R.drawable.ic_button_play_100)
            }

            override fun onPlay() {
                timer.start()
                trackPlayPauseButton.setImageResource(R.drawable.ic_button_pause_100)
            }

            override fun onPause() {
                timer.pause()
                trackPlayPauseButton.setImageResource(R.drawable.ic_button_play_100)
            }

            override fun onStop() {
                timer.stop()
                trackPlayPauseButton.setImageResource(R.drawable.ic_button_play_100)
            }
        }

        MediaPlayerAdapter(mediaPlayerListener)
    }

    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<AppCompatImageButton>(R.id.back).setOnClickListener {
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
        trackPlayPauseButton.isEnabled = false
        timerTrackPosition.text = Track.millisToMMSS(0)
        trackPlayPauseButton.setOnClickListener {
            if (player.isPlaying())
                player.pause()
            else
                player.play()
        }

        track.previewUrl?.let { player.open(it) }

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

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onTickTimer() {
        timerTrackPosition.text = Track.millisToMMSS(player.getCurrentPosition())
    }

    override fun onResetTimer() {
        timerTrackPosition.text = Track.millisToMMSS(0)
    }
}