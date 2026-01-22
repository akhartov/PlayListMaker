package com.practicum.playlistmaker.player.ui.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

class ServiceController(
    private val service: Service,
    private val foregroundServiceType: Int,
    private val notificationChannelId: String,
    private val notificationPriority: Int = NotificationCompat.PRIORITY_DEFAULT,
    channelName: String,
    channelDescription: String,
) {
    private val allowNotifications by lazy { checkNotificationPermissions(service) }

    init {
        if (allowNotifications)
            createNotificationChannel(channelName, channelDescription)
    }

    fun stopForeground() {
        ServiceCompat.stopForeground(service, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    fun startForeground(
        serviceNotificationId: Int,
        notificationBuildStep: (NotificationCompat.Builder) -> NotificationCompat.Builder
    ) {
        val builder = NotificationCompat.Builder(service, notificationChannelId)
            .setPriority(notificationPriority)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        ServiceCompat.startForeground(
            service,
            serviceNotificationId,
            notificationBuildStep(builder).build(),
            foregroundServiceType
        )
    }

    private fun createNotificationChannel(channelName: String, channelDescription: String) {
        val notificationManager =
            service.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val existingChannel = notificationManager.getNotificationChannel(notificationChannelId)
        if (existingChannel == null) {
            val channel = NotificationChannel(
                notificationChannelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelDescription

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun checkNotificationPermissions(service: Service): Boolean {
            return ContextCompat.checkSelfPermission(
                service,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}