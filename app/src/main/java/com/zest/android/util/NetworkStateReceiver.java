package com.zest.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class NetworkStateReceiver extends BroadcastReceiver {


    private static final String TAG = NetworkStateReceiver.class.getSimpleName();
    protected Set<OnNetworkStateReceiverListener> listeners;
    protected Boolean connected;

    public NetworkStateReceiver() {
        listeners = new HashSet<>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        connected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        Log.d(TAG, "notifyStateToAll() called");
        for (OnNetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(OnNetworkStateReceiverListener listener) {
        if (connected == null || listener == null) return;

        if (connected)
            listener.networkAvailable();
        else
            listener.networkUnavailable();
    }

    public void addListener(OnNetworkStateReceiverListener listener) {
        listeners.add(listener);
        //notifyState(listener);
    }

    public void removeListener(OnNetworkStateReceiverListener listener) {
        listeners.remove(listener);
    }

    public interface OnNetworkStateReceiverListener {

        void networkAvailable();

        void networkUnavailable();
    }
}
