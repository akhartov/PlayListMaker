package com.practicum.playlistmaker.player.ui.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log


import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.presentation.AudioPlayerControl
import com.practicum.playlistmaker.player.ui.presentation.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

internal class MusicService : Service(), AudioPlayerControl {
    companion object {
        const val TRACK_URL = "song_url"
        const val CONTENT_TITLE = "content_title"
        const val CONTENT_MESSAGE = "content_message"
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val SERVICE_NOTIFICATION_ID = 100
        private const val LOG_TAG = "MusicService"
    }

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())

    // Переменная для хранения MediaPlayer
    private var mediaPlayer: MediaPlayer? = null
    private var trackUrl = ""
    private var contentTitle = ""
    private var contentMessage = ""

    private var allowNotifications = false
    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    private val binder = MusicServiceBinder()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        trackUrl = intent?.getStringExtra(TRACK_URL) ?: ""
        contentTitle = intent?.getStringExtra(CONTENT_TITLE) ?: "No title"
        contentMessage = intent?.getStringExtra(CONTENT_MESSAGE) ?: "No message"

        initMediaPlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()

        return super.onUnbind(intent)
    }

    private fun checkNotificationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun moveToForeground() {
        if (_playerState.value is PlayerState.Playing)
            showNotification()
    }

    override fun moveToNormal() {
        hideNotification()
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return _playerState.asStateFlow()
    }

    // Инициализация ресурсов
    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()
        if (checkNotificationPermissions())
            allowNotifications = true

        Log.i(LOG_TAG, "Notifications allowed: ${allowNotifications}")
        if (allowNotifications)
            createNotificationChannel()
    }

    // Освобождение ресурсов
    override fun onDestroy() {
        releasePlayer()

        if (allowNotifications)
            deleteNotificationChannel()
    }

    fun showNotification() {
        if (allowNotifications)
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createNotification(),
                getForegroundServiceTypeConstant()
            )
    }

    fun hideNotification() {
        if (allowNotifications)
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(contentTitle)
            .setContentText(contentMessage)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Playlist Maker Music Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun deleteNotificationChannel() {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
    }

    private fun initMediaPlayer() {
        if (trackUrl.isEmpty()) return

        mediaPlayer?.setDataSource(this.trackUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Loaded()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            hideNotification()
            _playerState.value = PlayerState.Stopped()
        }
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        _playerState.value = PlayerState.Default()

        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }
}