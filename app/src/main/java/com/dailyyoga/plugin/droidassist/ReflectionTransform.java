package com.dailyyoga.plugin.droidassist;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 9:07 AM
 * @description:
 */
public class ReflectionTransform {

    private static void println(String args) {
        LogTransform.println(Log.DEBUG, ReflectionTransform.class.getName(), args);
    }

    public static Object invoke(String preArgs, Method method, Object obj, Object... args) {
        try {
            preArgs += "_invoke";

            println(preArgs + "_" + method.toString());

            if (method.equals(PrivacyApiTransform.LOCATION_GET_LAST_KNOWN_LOCATION)) {
                return PrivacyApiTransform.getLastKnownLocation(preArgs, (LocationManager) obj, (String) args[0]);
            } else if (method.equals(PrivacyApiTransform.PACKAGE_MANAGER_GET_INSTALLED_PACKAGES)) {
                return PrivacyApiTransform.getInstalledPackages(preArgs, (PackageManager) obj, (int) args[0]);
            } else if (method.equals(PrivacyApiTransform.PACKAGE_MANAGER_GET_INSTALLED_APPLICATIONS)) {
                return PrivacyApiTransform.getInstalledApplications(preArgs, (PackageManager) obj, (int) args[0]);
            } else if (method.equals(PrivacyApiTransform.SECURE_GET_STRING)) {
                return PrivacyApiTransform.getString(preArgs, (ContentResolver) args[0], (String) args[1]);
            } else if (method.equals(PrivacyApiTransform.TELEPHONY_MANAGER_GET_DEVICE_ID)) {
                return PrivacyApiTransform.getDeviceId(preArgs, (TelephonyManager) obj);
            } else if (method.equals(PrivacyApiTransform.TELEPHONY_MANAGER_GET_DEVICE_ID_SLOT_INDEX)) {
                return PrivacyApiTransform.getDeviceId(preArgs, (TelephonyManager) obj, (int) args[0]);
            } else if (method.equals(PrivacyApiTransform.TELEPHONY_MANAGER_GET_IMEI)) {
                return PrivacyApiTransform.getImei(preArgs, (TelephonyManager) obj);
            } else if (method.equals(PrivacyApiTransform.TELEPHONY_MANAGER_GET_IMEI_SLOT_INDEX)) {
                return PrivacyApiTransform.getImei(preArgs, (TelephonyManager) obj, (int) args[0]);
            } else if (method.equals(PrivacyApiTransform.TELEPHONY_MANAGER_GET_SUBSCRIBER_ID)) {
                return PrivacyApiTransform.getSubscriberId(preArgs, (TelephonyManager) obj);
            } else if (method.equals(PrivacyApiTransform.WIFI_INFO_GET_MAC_ADDRESS)) {
                return PrivacyApiTransform.getMacAddress(preArgs, (WifiInfo) obj);
            } else if (method.equals(PrivacyApiTransform.NETWORK_INTERFACE_GET_HARDWARE_ADDRESS)) {
                return PrivacyApiTransform.getHardwareAddress(preArgs, (NetworkInterface) obj);
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
}
