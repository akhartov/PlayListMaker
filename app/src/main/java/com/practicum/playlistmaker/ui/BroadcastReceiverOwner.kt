package com.practicum.playlistmaker.ui

import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BroadcastReceiverOwner(
    private val broadcastReceiver: NetworkBroadcastReceiver,
    private val filterAction: String
) {

    private var isRegistered = false
    fun register(context: Context?) {
        if(isRegistered)
            return

        context?.let {
            ContextCompat.registerReceiver(
                context,
                broadcastReceiver,
                IntentFilter(filterAction),
                ContextCompat.RECEIVER_NOT_EXPORTED,
            )
        }

        isRegistered = true
    }

    fun unregister(activity: FragmentActivity) {
        if(isRegistered)
            activity.unregisterReceiver(broadcastReceiver)

        isRegistered = false
    }
}