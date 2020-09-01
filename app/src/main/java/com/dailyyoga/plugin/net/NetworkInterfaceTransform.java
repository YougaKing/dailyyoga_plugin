package com.dailyyoga.plugin.net;

import android.util.Log;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 21:08
 * @description:
 */
public class NetworkInterfaceTransform {

    public static final String FAKE_MAC = "02:00:00:00:00:00";
    public static final String INVALID_MAC = "00:00:00:00:00:00";

    public static String getNetworkInterfaces(String args) {
        Log.e(NetworkInterfaceTransform.class.getName(), args);
        return "12321";
    }
}
