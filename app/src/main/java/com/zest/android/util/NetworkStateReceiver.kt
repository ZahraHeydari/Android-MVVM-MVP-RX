package com.zest.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import java.util.*

class NetworkStateReceiver : BroadcastReceiver() {

    protected var listeners: MutableSet<OnNetworkStateReceiverListener>
    protected var connected: Boolean? = null

    init {
        listeners = HashSet()
        connected = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        connected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        Log.d(TAG, "notifyStateToAll() called")
        for (listener in listeners)
            notifyState(listener)
    }

    private fun notifyState(listener: OnNetworkStateReceiverListener?) {
        if (connected == null || listener == null) return

        if (connected!!)
            listener.networkAvailable()
        else
            listener.networkUnavailable()
    }

    fun addListener(listener: OnNetworkStateReceiverListener) {
        listeners.add(listener)
        //notifyState(listener);
    }

    fun removeListener(listener: OnNetworkStateReceiverListener) {
        listeners.remove(listener)
    }

    interface OnNetworkStateReceiverListener {

        fun networkAvailable()

        fun networkUnavailable()
    }

    companion object {

        private val TAG = NetworkStateReceiver::class.java!!.getSimpleName()
    }
}
