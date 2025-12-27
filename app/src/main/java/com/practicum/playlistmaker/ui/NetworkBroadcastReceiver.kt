package com.practicum.playlistmaker.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY
import android.util.Log
import android.widget.Toast
import com.practicum.playlistmaker.R

class NetworkBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION) {
            val noConnectivity = intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false)


            Log.d("NetworkBroadcastReceiver", "${EXTRA_NO_CONNECTIVITY} is ${noConnectivity}")
            if (noConnectivity && context != null) {
                Toast.makeText(
                    context,
                    context.resources?.getString(R.string.no_connectivity),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val ACTION = CONNECTIVITY_ACTION
    }
}