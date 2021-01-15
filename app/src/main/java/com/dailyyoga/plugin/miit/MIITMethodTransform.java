package com.dailyyoga.plugin.miit;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 21:08
 * @description:
 */
public class MIITMethodTransform {

    private static void println(String args) {
        Log.e(MIITMethodTransform.class.getName(), args);
    }

    public static Object invoke(String preArgs, Method method, Object obj, Object... args) {
        println(preArgs);
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location getLastKnownLocation(String args, LocationManager locationManager, String provider) {
        println(args);
        return locationManager.getLastKnownLocation(provider);
    }

    public static List<PackageInfo> getInstalledPackages(String args, PackageManager packageManager, int flags) {
        println(args);
        return packageManager.getInstalledPackages(flags);
    }


    public static List<ApplicationInfo> getInstalledApplications(String args, PackageManager packageManager, int flags) {
        println(args);
        return packageManager.getInstalledApplications(flags);
    }

    public static String getString(String args, ContentResolver resolver, String name) {
        println(args);
        return Settings.Secure.getString(resolver, name);
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager) {
        println(args);
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.getDeviceId(slotIndex);
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager) {
        println(args);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return "";
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei(slotIndex);
        } else {
            return "";
        }
    }

    public static String getSubscriberId(String args, TelephonyManager telephonyManager) {
        println(args);
        return telephonyManager.getSubscriberId();
    }

    public static String getMacAddress(String args, WifiInfo wifiInfo) {
        println(args);
        return wifiInfo.getMacAddress();
    }

    public static byte[] getHardwareAddress(String args, NetworkInterface networkInterface) {
        println(args);
        try {
            return networkInterface.getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}

