package com.dailyyoga.plugin.droidassist;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
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

import com.dailyyoga.cn.YogaContext;
import com.dailyyoga.h2.permission.RxPermissions;
import com.dailyyoga.h2.util.PersistencePreferencesUtil;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static com.dailyyoga.h2.util.PersistencePreferencesUtil.TWO_LAUNCH_PRIVACY_POLICY_AGREE;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 21:08
 * @description:
 */
public class PrivacyApiTransform {

    public static Method LOCATION_GET_LAST_KNOWN_LOCATION;
    public static Method PACKAGE_MANAGER_GET_INSTALLED_PACKAGES;
    public static Method PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS;
    public static Method ACTIVITY_MANAGER_GET_RUNNING_TASKS;
    public static Method ACTIVITY_MANAGER_GET_RUNNING_APP_PROCESSES;
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

            aClass = ActivityManager.class;
            method = aClass.getDeclaredMethod("getRunningTasks", int.class);
            ACTIVITY_MANAGER_GET_RUNNING_TASKS = method;

            aClass = ActivityManager.class;
            method = aClass.getDeclaredMethod("getRunningAppProcesses");
            ACTIVITY_MANAGER_GET_RUNNING_APP_PROCESSES = method;

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

    private static void println(String args) {
        LogTransform.println(Log.ERROR, PrivacyApiTransform.class.getName(), args);
    }

    public static boolean agreePrivacyPolicy() {
        return PersistencePreferencesUtil.getBoolean(TWO_LAUNCH_PRIVACY_POLICY_AGREE);
    }

    public static Location getLastKnownLocation(String args, LocationManager locationManager, String provider) {
        println(args + "_" + LOCATION_GET_LAST_KNOWN_LOCATION.toString());
        if (!agreePrivacyPolicy()) return new Location(provider);
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)) {
            return new Location(provider);
        }
        return locationManager.getLastKnownLocation(provider);
    }

    public static List<PackageInfo> getInstalledPackages(String args, PackageManager packageManager, int flags) {
        println(args + "_" + PACKAGE_MANAGER_GET_INSTALLED_PACKAGES.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return getInstalledPackages(flags);
    }

    public static List<ApplicationInfo> getInstalledApplications(String args, PackageManager packageManager, int flags) {
        println(args + "_" + PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return getInstalledApplications(flags);
    }

    public static List<RunningTaskInfo> getRunningTasks(String args, ActivityManager activityManager, int maxNum) {
        println(args + "_" + ACTIVITY_MANAGER_GET_RUNNING_TASKS.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return activityManager.getRunningTasks(maxNum);
    }

    public static List<RunningAppProcessInfo> getRunningAppProcesses(String args, ActivityManager activityManager) {
        println(args + "_" + ACTIVITY_MANAGER_GET_RUNNING_APP_PROCESSES.toString());
        if (!agreePrivacyPolicy()) return new ArrayList<>();
        return activityManager.getRunningAppProcesses();
    }

    public static String getString(String args, ContentResolver resolver, String name) {
        println(args + "_" + SECURE_GET_STRING.toString());
        if (!agreePrivacyPolicy()) return "";
        return Secure.getString(resolver, name);
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_DEVICE_ID.toString());
        if (!agreePrivacyPolicy()) return "";
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), READ_PHONE_STATE)) {
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceId(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args + "_" + TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX.toString());
        if (!agreePrivacyPolicy()) return "";
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), READ_PHONE_STATE)) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.getDeviceId(slotIndex);
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_IMEI.toString());
        if (!agreePrivacyPolicy()) return "";
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), READ_PHONE_STATE)) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return "";
        }
    }

    public static String getImei(String args, TelephonyManager telephonyManager, int slotIndex) {
        println(args + "_" + TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX.toString());
        if (!agreePrivacyPolicy()) return "";
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), READ_PHONE_STATE)) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei(slotIndex);
        } else {
            return "";
        }
    }

    public static String getSubscriberId(String args, TelephonyManager telephonyManager) {
        println(args + "_" + TELEPHONY_MANAGER_GET_SUBSCRIBER_ID.toString());
        if (!agreePrivacyPolicy()) return "";
        if (!RxPermissions.checkSelfPermission(YogaContext.getContext(), READ_PHONE_STATE)) {
            return "";
        }
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

    private static List<PackageInfo> getInstalledPackages(int flags) {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        try {
            PackageInfo packageInfo = YogaContext.getContext().getPackageManager().getPackageInfo(YogaContext.getContext().getPackageName(), flags);
            packageInfoList.add(packageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfoList;
    }

    private static List<ApplicationInfo> getInstalledApplications(int flags) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        try {
            ApplicationInfo applicationInfo = YogaContext.getContext().getPackageManager().getApplicationInfo(YogaContext.getContext().getPackageName(), flags);
            applicationInfoList.add(applicationInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applicationInfoList;
    }

}

