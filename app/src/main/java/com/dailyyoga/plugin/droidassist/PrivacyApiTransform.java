package com.dailyyoga.plugin.droidassist;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 21:08
 * @description:
 */
public class PrivacyApiTransform {

    public static Method LOCATION_GET_LAST_KNOWN_LOCATION;
    public static Method PACKAGE_MANAGER_GET_INSTALLED_PACKAGES;
    public static Method PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS;
    public static Method SECURE_GET_STRING;
    public static Method TELEPHONY_MANAGER_GET_DEVICE_ID;
    public static Method TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX;
    public static Method TELEPHONY_MANAGER_GET_IMEI;
    public static Method TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX;
    public static Method TELEPHONY_MANAGER_GET_SUBSCRIBER_ID;
    public static Method WIFI_INFO_GET_MAC_ADDRESS;
    public static Method NETWORK_INTERFACE_GET_HARDWARE_ADDRESS;

    static {
        try {
            Class aClass = LocationManager.class;
            Method method = aClass.getDeclaredMethod("getLastKnownLocation", String.class);
            LOCATION_GET_LAST_KNOWN_LOCATION = method;

            aClass = PackageManager.class;
            method = aClass.getDeclaredMethod("getInstalledPackages", int.class);
            PACKAGE_MANAGER_GET_INSTALLED_PACKAGES = method;

            aClass = PackageManager.class;
            method = aClass.getDeclaredMethod("getInstalledApplications", int.class);
            PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS = method;

            aClass = Secure.class;
            method = aClass.getDeclaredMethod("getString", ContentResolver.class, String.class);
            SECURE_GET_STRING = method;

            aClass = TelephonyManager.class;
            method = aClass.getDeclaredMethod("getDeviceId");
            TELEPHONY_MANAGER_GET_DEVICE_ID = method;

            aClass = TelephonyManager.class;
            method = aClass.getDeclaredMethod("getDeviceId", int.class);
            TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX = method;

            aClass = TelephonyManager.class;
            method = aClass.getDeclaredMethod("getImei");
            TELEPHONY_MANAGER_GET_IMEI = method;

            aClass = TelephonyManager.class;
            method = aClass.getDeclaredMethod("getImei", int.class);
            TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX = method;

            aClass = TelephonyManager.class;
            method = aClass.getDeclaredMethod("getSubscriberId");
            TELEPHONY_MANAGER_GET_SUBSCRIBER_ID = method;

            aClass = WifiInfo.class;
            method = aClass.getDeclaredMethod("getMacAddress");
            WIFI_INFO_GET_MAC_ADDRESS = method;

            aClass = NetworkInterface.class;
            method = aClass.getDeclaredMethod("getHardwareAddress");
            NETWORK_INTERFACE_GET_HARDWARE_ADDRESS = method;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean agreePrivacyPolicy() {
        return false;
    }

    private static void println(String args) {
        LogTransform.println(Log.ERROR, PrivacyApiTransform.class.getName(), args);
    }

    public static Location getLastKnownLocation(String args, LocationManager locationManager, String provider) {
        println(args + "_" + LOCATION_GET_LAST_KNOWN_LOCATION.toString());
        if (!agreePrivacyPolicy()) return new Location(provider);
        return locationManager.getLastKnownLocation(provider);
    }

    public static List<PackageInfo> getInstalledPackages(String args, PackageManager packageManager, int flags) {
        println(args + "_" + PACKAGE_MANAGER_GET_INSTALLED_PACKAGES.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return packageManager.getInstalledPackages(flags);
    }

    public static List<ApplicationInfo> getInstalledApplications(String args, PackageManager packageManager, int flags) {
        println(args + "_" + PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return packageManager.getInstalledApplications(flags);
    }

    public static String getString(String args, ContentResolver resolver, String name) {
        println(args + "_" + SECURE_GET_STRING.toString());
        if (!agreePrivacyPolicy()) return "";
        return Secure.getString(resolver, name);
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_DEVICE_ID.toString());
        if (!agreePrivacyPolicy()) return "";
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args + "_" + TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX.toString());
        if (!agreePrivacyPolicy()) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.getDeviceId(slotIndex);
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_IMEI.toString());
        if (!agreePrivacyPolicy()) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return "";
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args + "_" + TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX.toString());
        if (!agreePrivacyPolicy()) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei(slotIndex);
        } else {
            return "";
        }
    }

    public static String getSubscriberId(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_SUBSCRIBER_ID.toString());
        if (!agreePrivacyPolicy()) return "";
        return telephonyManager.getSubscriberId();
    }

    public static String getMacAddress(String args, WifiInfo wifiInfo) {
        println(args + "_" + WIFI_INFO_GET_MAC_ADDRESS.toString());
        if (!agreePrivacyPolicy()) return "02:00:00:00:00:00";
        return wifiInfo.getMacAddress();
    }

    public static byte[] getHardwareAddress(String args, NetworkInterface networkInterface) {
        println(args + "_" + NETWORK_INTERFACE_GET_HARDWARE_ADDRESS.toString());
        try {
            if (!agreePrivacyPolicy()) return new byte[0];
            return networkInterface.getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}

