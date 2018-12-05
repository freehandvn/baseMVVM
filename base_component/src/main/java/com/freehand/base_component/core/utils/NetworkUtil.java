package com.freehand.base_component.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

/**
 * Created by minhpham on 6/29/17.
 * Purpose:
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public class NetworkUtil {

    public static final int NETWORK_STATUS_NOT_CONNECTED = 0, NETWORK_STAUS_WIFI = 1, NETWORK_STATUS_MOBILE = 2;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private static boolean offline = false;
    private static OnWorkModeListener onWorkModeListener;

    public interface OnWorkModeListener {
        void onSwitchMode(boolean offlineMode);
    }
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = NETWORK_STAUS_WIFI;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvaiable(Context context) {
        if(offline) return false;
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable());
        return isConnected;
    }

    public static void setOffline(boolean offline) {
        NetworkUtil.offline = offline;
        if (onWorkModeListener != null) onWorkModeListener.onSwitchMode(offline);
    }

    public static boolean isOfflineTrigger(){
        return offline;
    }

    public static void setWorkModeListener(OnWorkModeListener onWorkModeListener) {
        NetworkUtil.onWorkModeListener = onWorkModeListener;
    }

    public static void removeWorkModeListener() {
        onWorkModeListener = null;
    }
}
