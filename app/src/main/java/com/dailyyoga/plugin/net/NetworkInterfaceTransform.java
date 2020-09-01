package com.dailyyoga.plugin.net;

import android.util.Log;

import com.mob.tools.MobLog;

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
                byte[] mac;
                mac = intf.getHardwareAddress();

                if (mac != null) {
                    macs.put(intf.getName(), getNetworkInterfaces(args));
                }
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
        JSONObject var1 = new JSONObject();
//        TelephonyManager var2;
//        try {
//            var2 = (TelephonyManager) this.mContext.getSystemService("phone");
//            var1.put("imei", var2.getDeviceId());
//        } catch (Exception var15) {
//            if (var15 != null) {
//                EMLog.d("EMClient", var15.getMessage());
//            }
//        }
//
//        String var16 = null;
//
//        try {
//            ArrayList var3 = Collections.list(NetworkInterface.getNetworkInterfaces());
//            Iterator var4 = var3.iterator();
//
//            label63:
//            while (true) {
//                NetworkInterface var5;
//                do {
//                    if (!var4.hasNext()) {
//                        break label63;
//                    }
//
//                    var5 = (NetworkInterface) var4.next();
//                } while (!var5.getName().equalsIgnoreCase("wlan0"));
//
//                byte[] var6 = var5.getHardwareAddress();
//                if (var6 == null) {
//                    var2 = null;
//                }
//
//                StringBuilder var7 = new StringBuilder();
//                byte[] var8 = var6;
//                int var9 = var6.length;
//
//                for (int var10 = 0; var10 < var9; ++var10) {
//                    byte var11 = var8[var10];
//                    var7.append(Integer.toHexString(var11 & 255) + ":");
//                }
//
//                if (var7.length() > 0) {
//                    var7.deleteCharAt(var7.length() - 1);
//                }
//
//                var16 = var7.toString();
//            }
//        } catch (Exception var14) {
//            EMLog.d("EMClient", var14.getMessage());
//        }
//
//        WindowManager var17 = (WindowManager) this.mContext.getSystemService("window");
//        int var18 = 0;
//        int var19 = 0;
//        double var20 = 0.0D;
//
//        try {
//            DisplayMetrics var21 = new DisplayMetrics();
//            var17.getDefaultDisplay().getMetrics(var21);
//            var18 = var21.widthPixels;
//            var19 = var21.heightPixels;
//            var20 = (double) var21.densityDpi;
//        } catch (Exception var13) {
//            EMLog.d("EMClient", var13.getMessage());
//        }
//
//        DeviceUuidFactory var22 = new DeviceUuidFactory(this.mContext);
//        String var23 = var22.getDeviceUuid().toString();
//        String var24 = Settings.Secure.getString(this.mContext.getContentResolver(), "android_id");
//
//        try {
//            var1.put("deviceid", var23);
//            var1.put("android-id", var24);
//            var1.put("app-id", this.mContext.getPackageName());
//            var1.put("hid", getInstance().getCurrentUser());
//            var1.put("os", "android");
//            var1.put("os-version", android.os.Build.VERSION.RELEASE);
//            var1.put("manufacturer", Build.MANUFACTURER);
//            var1.put("model", Build.MODEL);
//            var1.put("width", var18);
//            var1.put("height", var19);
//            var1.put("dpi", var20);
//            if (var16 != null) {
//                var1.put("wifi-mac-address", var16);
//            }
//        } catch (JSONException var12) {
//            EMLog.d("EMClient", var12.getMessage());
//        }
        return var1;
    }

    private static String byteToHex(byte[] mac) {
        if (mac == null) {
            return null;
        } else {
            StringBuilder buf = new StringBuilder();
            byte[] var3 = mac;
            int var4 = mac.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                byte aMac = var3[var5];
                buf.append(String.format("%02x:", aMac));
            }

            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }
}
