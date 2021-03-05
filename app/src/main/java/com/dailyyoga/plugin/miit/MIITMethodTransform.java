package com.dailyyoga.plugin.miit;

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

import java.lang.reflect.InvocationTargetException;
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
public class MIITMethodTransform {

    private static Method LOCATION_GET_LAST_KNOWN_LOCATION;
    private static Method PACKAGE_MANAGER_GET_INSTALLED_PACKAGES;
    private static Method PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS;
    private static Method SECURE_GET_STRING;
    private static Method TELEPHONY_MANAGER_GET_DEVICE_ID;
    private static Method TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX;
    private static Method TELEPHONY_MANAGER_GET_IMEI;
    private static Method TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX;
    private static Method TELEPHONY_MANAGER_GET_SUBSCRIBER_ID;
    private static Method WIFI_INFO_GET_MAC_ADDRESS;
    private static Method NETWORK_INTERFACE_GET_HARDWARE_ADDRESS;

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
        Log.e(MIITMethodTransform.class.getName(), args);
    }

    public static Object invoke(String preArgs, Method method, Object obj, Object... args) {
        try {
            preArgs += "_invoke";

            Log.d(MIITMethodTransform.class.getName(), preArgs + "_" + method.toString());

            if (method.equals(LOCATION_GET_LAST_KNOWN_LOCATION)) {
                return getLastKnownLocation(preArgs, (LocationManager) obj, (String) args[0]);
            } else if (method.equals(PACKAGE_MANAGER_GET_INSTALLED_PACKAGES)) {
                return getInstalledPackages(preArgs, (PackageManager) obj, (int) args[0]);
            } else if (method.equals(PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS)) {
                return getInstalledApplications(preArgs, (PackageManager) obj, (int) args[0]);
            } else if (method.equals(SECURE_GET_STRING)) {
                return getString(preArgs, (ContentResolver) args[0], (String) args[1]);
            } else if (method.equals(TELEPHONY_MANAGER_GET_DEVICE_ID)) {
                return getDeviceId(preArgs, (TelephonyManager) obj);
            } else if (method.equals(TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX)) {
                return getDeviceId(preArgs, (TelephonyManager) obj, (int) args[0]);
            } else if (method.equals(TELEPHONY_MANAGER_GET_IMEI)) {
                return getImei(preArgs, (TelephonyManager) obj);
            } else if (method.equals(TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX)) {
                return getImei(preArgs, (TelephonyManager) obj, (int) args[0]);
            } else if (method.equals(TELEPHONY_MANAGER_GET_SUBSCRIBER_ID)) {
                return getSubscriberId(preArgs, (TelephonyManager) obj);
            } else if (method.equals(WIFI_INFO_GET_MAC_ADDRESS)) {
                return getMacAddress(preArgs, (WifiInfo) obj);
            } else if (method.equals(NETWORK_INTERFACE_GET_HARDWARE_ADDRESS)) {
                return getHardwareAddress(preArgs, (NetworkInterface) obj);
            } else {
                return method.invoke(obj, args);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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

