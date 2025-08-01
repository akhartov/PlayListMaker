package com.practicum.playlistmaker.presentation.player

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.api.AudioPlayer
import com.practicum.playlistmaker.domain.models.Track


class PlayerActivity : AppCompatActivity(), TimerTickListener {
    companion object {
        val TRACK = "TRACK"
    }

    private lateinit var binding: ActivityPlayerBinding
    private val player by lazy {
        Creator.getAudioPlayerProvider().provideAudioPlayer(object : AudioPlayer.Listener {
            override fun onReadyToPlay() {
                binding.buttonPlay.isEnabled = true
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
            }

            override fun onPlay() {
                timer.start()
                binding.buttonPlay.setImageResource(R.drawable.ic_button_pause_100)
            }

            override fun onPause() {
                timer.pause()
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
            }

            override fun onStop() {
                timer.stop()
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
            }
        })
    }

    private val track by lazy {
        Gson().fromJson<Track>(intent.getStringExtra(TRACK), object : TypeToken<Track>() {}.type)
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    val timer = LooperTimer(mainThreadHandler, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.trackTitle.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackLength.text = track.trackTimeMMSS()

        binding.albumGroup.isVisible = !track.collectionName.isNullOrEmpty()
        binding.trackAlbum.text = track.collectionName

        binding.yearGroup.isVisible = track.trackYear().isNotEmpty()
        binding.trackYear.text = track.trackYear()

        binding.genreGroup.isVisible = !track.primaryGenreName.isNullOrEmpty()
        binding.trackGenre.text = track.primaryGenreName

        binding.trackCountry.text = track.country
        binding.trackTimePosition.text = Track.millisToMMSS(0)

        binding.buttonPlay.apply {
            isEnabled = false
            setOnClickListener {
                if (player.isPlaying())
                    player.pause()
                else
                    player.play()
            }
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
            .into(binding.cover)
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
        binding.trackTimePosition.text = Track.millisToMMSS(player.getCurrentPosition())
    }

    override fun onResetTimer() {
        binding.trackTimePosition.text = Track.millisToMMSS(0)
    }
}