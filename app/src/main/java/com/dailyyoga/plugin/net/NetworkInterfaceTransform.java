package com.dailyyoga.plugin.net;

import android.util.Log;

import com.mob.tools.MobLog;

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
