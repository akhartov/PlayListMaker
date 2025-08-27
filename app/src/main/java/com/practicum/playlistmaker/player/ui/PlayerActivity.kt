package com.practicum.playlistmaker.player.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(getTrackFromIntent())
    }

    private fun getTrackFromIntent(): Track? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(TRACK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getStateLiveData().observe(this) { state ->
            when (state) {
                PlayerState.Pause -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }

                PlayerState.Play -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_pause_100)
                }

                PlayerState.ReadyToPlay -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }

                PlayerState.Stop -> {
                    binding.buttonPlay.setImageResource(R.drawable.ic_button_play_100)
                }
            }
        }

        viewModel.getTrackPositionLiveData().observe(this) { trackTimePosition ->
            binding.trackTimePosition.text = trackTimePosition
        }

        viewModel.getTrackLiveData().observe(this) {
            it?.let { track ->
                binding.trackTitle.text = track.trackName
                binding.trackArtist.text = track.artistName
                binding.trackLength.text = track.length

                binding.albumGroup.isVisible = track.collectionName.isNotEmpty()
                binding.trackAlbum.text = track.collectionName

                binding.yearGroup.isVisible = track.trackYear.isNotEmpty()
                binding.trackYear.text = track.trackYear

                binding.genreGroup.isVisible = track.primaryGenreName.isNotEmpty()
                binding.trackGenre.text = track.primaryGenreName

                binding.trackCountry.text = track.country

                loadImage(track.coverArtwork)
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.clickPlay()
        }

        binding.buttonLike.setOnClickListener {
            viewModel.likeCurrentTrack()
        }

        binding.buttonFavourite.setOnClickListener {
            viewModel.addCurrentTrackToFavourites()
        }
    }

    private fun loadImage(trackUrl: String) {
        Glide.with(applicationContext).load(Uri.parse(trackUrl))
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
        viewModel.pause()
    }

    companion object {
        val TRACK = "TRACK"
    }
}