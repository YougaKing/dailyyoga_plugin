package com.dailyyoga.plugin.net;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.hyphenate.util.DeviceUuidFactory;
import com.mob.tools.MobLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;
import static com.hyphenate.chat.EMClient.getInstance;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 21:08
 * @description:
 */
public class NetworkInterfaceTransform {

    public static String getNetworkInterfaces(String args) {
        Log.e(NetworkInterfaceTransform.class.getName(), args);
        return "";
    }

    public static String getMacAddr(String wlan, String args) {
        return "";
    }

    public static String getMac(String args) {
        return "";
    }

    /**
     * @link com.mob.tools.utils.DeviceHelper.listNetworkHardware
     */
    public static HashMap<String, String> listNetworkHardware(String args) throws Throwable {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        if (nis == null) {
            return null;
        } else {
            List<NetworkInterface> interfaces = Collections.list(nis);
            HashMap<String, String> macs = new HashMap<>();

            for (NetworkInterface intf : interfaces) {

                macs.put(intf.getName(), getNetworkInterfaces(args));
            }
            return macs;
        }
    }

    /**
     * @link com.mob.tools.utils.DeviceHelper.getLocalIpInfo
     */
    public static ArrayList<HashMap<String, Object>> getLocalIpInfo(String args) {
        try {
            ArrayList<HashMap<String, Object>> resList = new ArrayList<>();
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            List<NetworkInterface> interfaces = Collections.list(nis);
            Iterator var5 = interfaces.iterator();

            while (true) {
                HashMap<String, Object> tmpRes;
                List list;
                do {
                    do {
                        if (!var5.hasNext()) {
                            return resList;
                        }

                        NetworkInterface intf = (NetworkInterface) var5.next();
                        tmpRes = new HashMap<>();
                        tmpRes.put("name", intf.getName());
                        tmpRes.put("mac", getNetworkInterfaces(args));
                        list = intf.getInterfaceAddresses();
                    } while (list == null);
                } while (list.size() <= 0);

                ArrayList<HashMap<String, Object>> tmpList = new ArrayList<>();

                HashMap<String, Object> tmpMap;
                for (Iterator var10 = list.iterator(); var10.hasNext(); tmpList.add(tmpMap)) {
                    InterfaceAddress ia = (InterfaceAddress) var10.next();
                    tmpMap = new HashMap<>();
                    InetAddress inetAddress = ia.getAddress();
                    tmpMap.put("haddr", inetAddress.getHostAddress());
                    tmpMap.put("hname", inetAddress.getHostName());
                    tmpMap.put("lp", inetAddress.isLoopbackAddress());
                    tmpMap.put("addr", byteToHex(inetAddress.getAddress()));
                    tmpMap.put("len", inetAddress.getAddress().length);
                    inetAddress = ia.getBroadcast();
                    if (inetAddress != null) {
                        HashMap<String, Object> broadcast = new HashMap<>();
                        broadcast.put("haddrB", inetAddress.getHostAddress());
                        broadcast.put("hnameB", inetAddress.getHostName());
                        broadcast.put("lpB", inetAddress.isLoopbackAddress());
                        broadcast.put("addrB", byteToHex(inetAddress.getAddress()));
                        broadcast.put("lenB", inetAddress.getAddress().length);
                        tmpMap.put("broadcast", broadcast);
                    }
                }

                tmpRes.put("inets", tmpList);
                resList.add(tmpRes);
            }
        } catch (Throwable throwable) {
            MobLog.getInstance().d(throwable);
        }
        return null;
    }

    /**
     * @link com.hyphenate.chat.EMClient.getDeviceInfo
     */
    public static JSONObject getDeviceInfo(String args) {
        JSONObject jsonObject = new JSONObject();
        String androidId = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        try {
            jsonObject.put("imei", androidId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String mac = getNetworkInterfaces(args);

        WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        int widthPixels = 0;
        int heightPixels = 0;
        double densityDpi = 0.0D;

        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            widthPixels = displayMetrics.widthPixels;
            heightPixels = displayMetrics.heightPixels;
            densityDpi = displayMetrics.densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(getContext());
        String deviceUuid = deviceUuidFactory.getDeviceUuid().toString();

        try {
            jsonObject.put("deviceid", deviceUuid);
            jsonObject.put("android-id", androidId);
            jsonObject.put("app-id", getContext().getPackageName());
            jsonObject.put("hid", getInstance().getCurrentUser());
            jsonObject.put("os", "android");
            jsonObject.put("os-version", android.os.Build.VERSION.RELEASE);
            jsonObject.put("manufacturer", Build.MANUFACTURER);
            jsonObject.put("model", Build.MODEL);
            jsonObject.put("width", widthPixels);
            jsonObject.put("height", heightPixels);
            jsonObject.put("dpi", densityDpi);
            jsonObject.put("wifi-mac-address", mac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static String byteToHex(byte[] mac) {
        if (mac == null) {
            return null;
        } else {
            StringBuilder buf = new StringBuilder();

            for (byte aMac : mac) {
                buf.append(String.format("%02x:", aMac));
            }

            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }

    private static Context getContext() {
        return null;
    }

    public static boolean onCreate(String args) {
        Log.e(NetworkInterfaceTransform.class.getName(), args);
        return false;
    }

    public static void init(String args) {
        Log.e(NetworkInterfaceTransform.class.getName(), args);
    }
}
