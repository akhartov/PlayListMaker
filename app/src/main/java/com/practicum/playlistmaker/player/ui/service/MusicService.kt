package com.practicum.playlistmaker.player.ui.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.presentation.AudioPlayerControl
import com.practicum.playlistmaker.player.ui.presentation.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

internal class MusicService : Service(), AudioPlayerControl {
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private var mediaPlayer: MediaPlayer? = null
    private var trackUrl = ""
    private var contentTitle = ""
    private var contentMessage = ""
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    private fun startTimer() {
        serviceScope.launch {
            while (mediaPlayer?.isPlaying == true) {
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
                delay(TRACK_PROGRESS_TIMEOUT)
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    private val binder = MusicServiceBinder()
    private var serviceController: ServiceController? = null

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        trackUrl = intent?.getStringExtra(TRACK_URL) ?: ""
        contentTitle = intent?.getStringExtra(CONTENT_TITLE) ?: "No title"
        contentMessage = intent?.getStringExtra(CONTENT_MESSAGE) ?: "No message"

        serviceController = ServiceController(
            service = this,
            foregroundServiceType = getForegroundServiceTypeConstant(),
            notificationChannelId = NOTIFICATION_CHANNEL_ID,
            channelName = "Playlist Maker Music Service",
            channelDescription = "Service for playing music",
        )

        initMediaPlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        serviceController = null
        return super.onUnbind(intent)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun moveToForeground() {
        if (_playerState.value is PlayerState.Playing)
            serviceController?.startForeground(SERVICE_NOTIFICATION_ID) { builder ->
                builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(contentTitle)
                    .setContentText(contentMessage)
            }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return _playerState.asStateFlow()
    }

    // Инициализация ресурсов
    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        releasePlayer()
    }

    override fun hideNotification() {
        serviceController?.stopForeground()
    }

    private fun initMediaPlayer() {
        if (trackUrl.isEmpty()) return

        mediaPlayer?.setDataSource(this.trackUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Loaded()
        }
        mediaPlayer?.setOnCompletionListener {
            hideNotification()
            _playerState.value = PlayerState.Stopped()
        }
    }

    private fun releasePlayer() {
        _playerState.value = PlayerState.Default()

        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        const val TRACK_URL = "song_url"
        const val CONTENT_TITLE = "content_title"
        const val CONTENT_MESSAGE = "content_message"

        private const val SERVICE_NOTIFICATION_ID = 100
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"

        private const val TRACK_PROGRESS_TIMEOUT = 300L
    }
}